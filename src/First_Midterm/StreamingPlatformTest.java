package First_Midterm;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


abstract class Item implements Comparable<Item> {
    protected String name;
    protected List<String> genres;

    public Item(String name, List<String> genres) {
        this.name = name;
        this.genres = genres;
    }

    abstract double calculateRating();
}

class Movie extends Item {

    private List<Double> ratings;

    public Movie(String name, List<String> genres, List<Double> rating) {
        super(name, genres);
        this.ratings = rating;
    }

    @Override
    public double calculateRating() {
        Double avgRating = ratings.stream().mapToDouble(Double::valueOf).average().orElse(0.0);
        Double factor = Math.min(ratings.size() / 20.0, 1.0);
        //min(број_на_рејтнизи/20.0,1.0)
        return avgRating * factor;
    }

    public List<String> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f", name, calculateRating());
    }

    @Override
    public int compareTo(Item other) {
        return Double.compare(this.calculateRating(), other.calculateRating());
    }
}

class Series extends Item {

    private List<String> episodes;
    private List<Double> ratingPerEpisode;

    public Series(String name, List<String> genres,
                  List<String> episodes, List<Double> ratingPerEpisode) {
        super(name, genres);
        this.episodes = episodes;
        this.ratingPerEpisode = ratingPerEpisode;
    }


    public List<String> getGenres() {
        return genres;
    }

    @Override
    public double calculateRating() {
        return ratingPerEpisode.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)", name, calculateRating(), ratingPerEpisode.size());
    }

    @Override
    public int compareTo(Item other) {
        return Double.compare(this.calculateRating(), other.calculateRating());
    }
}

class StreamingPlatform {
    private List<Item> items;

    public StreamingPlatform() {
        items = new ArrayList<>();
    }

    //Spider-Man: No Way Home;   Action,Adventure,Sci-Fi;     8 9 7 9 10 8 10 9 10 8 8 9 9 10 10 8 9 10 8 9 10 8 8 9 10
    //Friends;Comedy,Romance;S1E1 9 9 8 8 10 9 8 9 10 8 10 8 9 10 9 8 9 8 10 9 10 8 9 10 8;
    // S1E2 8 9 8 10 9 8 9 10 8 9 7 7 7 7 8 8 9 9 9 9;
    public void addItem(String data) {
        String[] parts = data.split(";");
        String name = parts[0];
        List<Double> avgEpisodeRatings = new ArrayList<>();
        List<String> episodes = new ArrayList<>();
        List<Integer> numRatingsPerEpisode = new ArrayList<>();

        List<String> genres = Arrays.stream(parts[1].split(",")).map(String::new).collect(Collectors.toList());

        if (!parts[2].startsWith("S")) {
            //movie
            List<Double> ratings = Arrays.stream(parts[2].split(" ")).map(s -> Double.parseDouble(s)).collect(Collectors.toList());
            Movie movie = new Movie(name, genres, ratings);
            items.add(movie);
        } else {
            for (int i = 2; i < parts.length; i++) {
                //odi od prvata epizoda do kraj na site drugi epizodi

                episodes.add(parts[i].split("\\s+")[0]);

                Double averageRating = Arrays.stream(parts[i].split("\\s+"))
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .average()
                        .orElse(0.0);

                double factor = Math.min(((parts[i].split("\\s+")).length-1) / 20.0, 1.0);
                avgEpisodeRatings.add(averageRating*factor);
            }
            Series series = new Series(name, genres, episodes, avgEpisodeRatings);
            items.add(series);
        }

    }

    public void listAllItems(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        //da gi pechati site items vo opagacki redosled spored rejting
        items.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(i -> pw.println(i.toString()));

        pw.flush();
    }

    public void listFromGenre(String genre, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        items.stream()
                .filter(i -> i.genres.contains(genre))
                .sorted(Comparator.reverseOrder())
                .forEach(match -> pw.println(match.toString()));

        pw.flush();
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}
