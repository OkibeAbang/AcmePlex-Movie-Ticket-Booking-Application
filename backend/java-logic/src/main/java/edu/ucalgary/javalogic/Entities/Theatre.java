package edu.ucalgary.javalogic.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "num_rows")
    private Integer num_rows;

    @Column(name = "max_seats_per_row")
    private Integer max_seats_per_row;

    @ManyToMany
    @JoinTable(name = "theatre_showing",
        joinColumns = @JoinColumn(name = "theatre_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id") )
    private Set<Movie> movies = new HashSet<>();

    public Theatre() {}

    public Theatre(String name, String address, Integer numRows, Integer maxSeatsPerRow) {
        this.name = name;
        this.address = address;
        this.num_rows = numRows;
        this.max_seats_per_row = maxSeatsPerRow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNum_rows() {
        return num_rows;
    }

    public void setNum_rows(Integer numRows) {
        this.num_rows = numRows;
    }

    public Integer getMax_seats_per_row() {
        return max_seats_per_row;
    }

    public void setMax_seats_per_row(Integer maxSeatsPerRow) {
        this.max_seats_per_row = maxSeatsPerRow;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public void removeMovie(Movie movie){
        this.movies.remove(movie);
    }
}
