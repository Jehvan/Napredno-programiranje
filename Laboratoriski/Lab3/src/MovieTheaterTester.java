import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}

class Movie {
    String title;
    String genre;
    int year;
    double avgRating;
    public Movie(String title, String genre, int year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    void calculateAvgRating(List<Integer> ratings) {
        for  (Integer rating : ratings) {
            avgRating += rating;
        }
        avgRating /= ratings.size();
    }

    @Override
    public String toString() {
        return title + ", " + genre + ", " + year + ", " + String.format("%.2f", avgRating);
    }
}


class MovieTheater {
    ArrayList<Movie> movies;

    public MovieTheater() {
        movies = new ArrayList<>();
    }

    void readMovies(InputStream in) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        int count = Integer.parseInt(br.readLine());
        while (count != 0) {
            count--;
            Movie tmp = new Movie(br.readLine(), br.readLine(), Integer.parseInt(br.readLine()));
            movies.add(tmp);
            List<Integer> ratings = new ArrayList<>();
            String ratingsStr = br.readLine();
            String [] parts = ratingsStr.split(" ");
            for (String part : parts) {
                ratings.add(Integer.parseInt(part));
            }
            tmp.calculateAvgRating(ratings);
        }
        br.close();
    }

    void printByGenreAndTitle() {
        List<Movie> tmp = movies.stream().sorted(Comparator.comparing((Movie a) -> a.genre).thenComparing(a -> a.title)).collect(Collectors.toList());
        for (Movie movie : tmp) {
            System.out.println(movie.toString());
        }
    }

    void printByYearAndTitle() {
        List<Movie> tmp  = movies.stream().sorted(Comparator.comparing((Movie a) -> a.year).thenComparing(a -> a.title)).collect(Collectors.toList());
        for (Movie movie : tmp) {
            System.out.println(movie.toString());
        }
    }

    void printByRatingAndTitle() {
        List<Movie> tmp = movies.stream().sorted(Comparator.comparing((Movie a) -> a.avgRating).reversed().thenComparing(a -> a.title)).collect(Collectors.toList());
        for (Movie movie : tmp) {
            System.out.println(movie.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Movie movie : movies) {
            out.append(movie.toString()).append("\n");
        }
        return out.toString();
    }
}