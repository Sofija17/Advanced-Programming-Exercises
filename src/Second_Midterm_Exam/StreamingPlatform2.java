package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}


public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id, name);
            } else if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id, name);
            } else if (parts[0].equals("addRating")) {
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")) {
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}

class Movie {
    private String id;
    private String name;
    private List<Rating> ratings;

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
        this.ratings = new ArrayList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    double calculateAvgMovieRating() {
        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average().orElse(0.0);

    }

    public static List<String> getFavoriteMoviesForUser(List<Rating> ratingsForUser) {

        int max = ratingsForUser.stream()
                .mapToInt(Rating::getRating)
                .max()
                .orElse(Integer.MIN_VALUE);

        return ratingsForUser.stream()
                .filter(r -> r.getRating() == max)
                .map(Rating::getMovieId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
//       Movie ID: 1 Title: Test1 Rating: 8.00
        return String.format("Movie ID: %s Title: %s Rating: %.2f", id, name, calculateAvgMovieRating());
    }
}

class User {
    private String id;
    private String username;
    private Map<String, Integer> ratingByMovie;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.ratingByMovie = new HashMap<>();
    }

    public void addToMap(String movieId, Integer rating){
        this.ratingByMovie.putIfAbsent(movieId, rating);
    }

    public Map<String, Integer> getRatingByMovie() {
        return ratingByMovie;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("User ID: %s Name: %s", id, username);
    }
}

class Rating {
    private String userId;
    private String movieId;
    private int rating;

    public Rating(String userId, String movieId, int rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getRating() {
        return rating;
    }
}

class StreamingPlatform {
    private Set<Movie> movies;
    private Set<User> users;
    private List<Rating> ratings;

    private Map<String, List<Rating>> byMovies;
    private Map<String, List<Rating>> byUser;


    public StreamingPlatform() {
        this.movies = new HashSet<>();
        this.users = new HashSet<>();
        this.ratings = new ArrayList<>();

        this.byMovies = new HashMap<>();
        this.byUser = new HashMap<>();
    }

    void addMovie(String id, String name) {
        Movie m = new Movie(id, name);
        this.movies.add(m);

        byMovies.putIfAbsent(id, new ArrayList<>());
    }

    void addUser(String id, String username) {
        User u = new User(id, username);
        this.users.add(u);

        byUser.putIfAbsent(id, new ArrayList<>());
    }

    void addRating(String userId, String movieId, int rating) {
        Rating r = new Rating(userId, movieId, rating);
        this.ratings.add(r);

        Movie movie = movies.stream()
                .filter(m -> m.getId().equals(movieId))
                .findFirst().get();
        movie.getRatings().add(r);

        User e = users.stream().filter(u -> u.getId().equals(userId)).findFirst().get();
        e.addToMap(movieId,rating);

        byMovies.get(movieId).add(r);
        byUser.get(userId).add(r);

    }

    void topNMovies(int n) {
        movies.stream()
                .sorted(Comparator.comparing(Movie::calculateAvgMovieRating).reversed())
                .limit(n)
                .forEach(System.out::println);
    }

    void favouriteMoviesForUsers(List<String> userIds) {
        Map<String, List<String>> favsByUser = byUser.entrySet().stream()
                .filter(e -> userIds.contains(e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Movie.getFavoriteMoviesForUser(e.getValue())
                ));

        for (Map.Entry<String, List<String>> e : favsByUser.entrySet()) {
            String userId = e.getKey();
            List<String> favMovieIds = e.getValue();

            User u = users.stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst().get();

            System.out.println(u);

            movies.stream()
                    .filter(m -> favMovieIds.contains(m.getId()))
                    .sorted(Comparator
                            .comparing(Movie::calculateAvgMovieRating).reversed()
                            .thenComparing(Movie::getName))
                    .forEach(System.out::println);

            System.out.println();
        }
    }

    void similarUsers(String userId){

        User toCompare = users.stream().filter(u -> u.getId().equals(userId)).findFirst().get();

        User mostSimilarUser = users.stream()
                .filter(u -> !u.getId().equals(userId))
                .max((u1, u2) -> {
                    double sim1 = CosineSimilarityCalculator.cosineSimilarity(u1.getRatingByMovie(), toCompare.getRatingByMovie());
                    double sim2 = CosineSimilarityCalculator.cosineSimilarity(u2.getRatingByMovie(), toCompare.getRatingByMovie());

                    return Double.compare(sim1, sim2);
                })
                .orElse(null);

        System.out.println(mostSimilarUser + " " + CosineSimilarityCalculator.cosineSimilarity(mostSimilarUser.getRatingByMovie(), toCompare.getRatingByMovie()));


    }
}
