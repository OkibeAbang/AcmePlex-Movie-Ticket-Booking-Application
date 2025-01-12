package edu.ucalgary.javalogic.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl; // URL or file path to the movie poster

    // Constructors
    public News() {
    }

    public News(Movie movie, String posterUrl) {
        this.movie = movie;
        this.posterUrl = posterUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
