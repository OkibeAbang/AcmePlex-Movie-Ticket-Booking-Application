package DomainLayer;

import java.util.ArrayList;

public class Theatre {
    private int numRows;
    private int maxSeatsPerRow;
    private String address;
    private ArrayList<Movie> movies;

    public Theatre(int numRows, int maxSeatsPerRow, String address) {
        this.numRows = numRows;
        this.maxSeatsPerRow = maxSeatsPerRow;
        this.address = address;
        this.movies = new ArrayList<>();
    }

    public int getNumRows() {
        return numRows;
    }

    public int getMaxSeatsPerRow() {
        return maxSeatsPerRow;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public String getAddress() {
        return address;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }
 }
