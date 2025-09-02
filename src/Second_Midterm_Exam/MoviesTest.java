//package Second_Midterm_Exam;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class MoviesTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        MoviesList moviesList = new MoviesList();
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        for (int i = 0; i < n; ++i) {
//            String title = scanner.nextLine();
//            int x = scanner.nextInt();
//            int[] ratings = new int[x];
//            for (int j = 0; j < x; ++j) {
//                ratings[j] = scanner.nextInt();
//            }
//            scanner.nextLine();
//            moviesList.addMovie(title, ratings);
//        }
//        scanner.close();
//        List<Movie> movies = moviesList.top10ByAvgRating();
//        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
//        for (Movie movie : movies) {
//            System.out.println(movie);
//        }
//        movies = moviesList.top10ByRatingCoef();
//        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
//        for (Movie movie : movies) {
//            System.out.println(movie);
//        }
//    }
//}
//
//// vashiot kod ovde
//class Movie{
//    private String title;
//    private List<Integer> ratings;
//
//    public Movie(String title, List<Integer> ratings) {
//        this.title = title;
//        this.ratings = ratings;
//    }
//
//    public double calculateRating(){
//       double sum = ratings.stream()
//               .mapToInt(rating -> rating)
//               .sum();
//
//       return sum/(double)ratings.size();
//    }
//
//    public double calculateCoeff(){
//        return calculateRating() * ratings.size();
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public List<Integer> getRatings() {
//        return ratings;
//    }
//
//
//
//    //Story of Women (1989) (6.63) of 8 ratings
//    @Override
//    public String toString() {
//        return String.format("%s (%.2f) of %d ratings", title, calculateRating(), ratings.size());
//    }
//}
//
//class MoviesList{
//    private List<Movie> movies;
//
//    public MoviesList() {
//        this.movies = new ArrayList<>();
//    }
//    public void addMovie(String title, int[] ratings){
//        List<Integer> list = Arrays.stream(ratings).boxed().collect(Collectors.toList());
//        this.movies.add(new Movie(title,list));
//
//    }
//    public List<Movie> top10ByAvgRating(){
//        Comparator<Movie> comparator = Comparator.comparing(Movie::calculateRating).reversed().thenComparing(Movie::getTitle);
//
//        return movies.stream()
//                .sorted(comparator)
//                .limit(10)
//                .collect(Collectors.toList());
//    }
//
//    public List<Movie> top10ByRatingCoef(){
//
//        Comparator<Movie> comparator = Comparator.comparingDouble((Movie movie) -> movie.calculateCoeff() / movies.size())
//                                                    .reversed()
//                                                    .thenComparing(Movie::getTitle);
//        return movies.stream()
//                .sorted(comparator)
//                .limit(10)
//                .collect(Collectors.toList());
//    }
//}