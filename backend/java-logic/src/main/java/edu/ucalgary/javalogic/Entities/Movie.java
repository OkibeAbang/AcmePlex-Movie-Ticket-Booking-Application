package edu.ucalgary.javalogic.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "duration")
    private String duration;

    @Column(name = "age_rating")
    private String ageRating;

    @Column(name = "release_date_public")
    private Date releaseDatePublic;

    @Column(name = "release_date_premium")
    private Date releaseDatePremium;

    @Column(name = "genre")
    private String genre;

    @Column(name = "poster_file_path")
    private String posterFilePath;

    @Column(name = "featured")
    private Boolean featured;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "movie_id")
    private Set<Showtime> showtimes = new HashSet<>();

    @ManyToMany(mappedBy = "movies")
    @JsonIgnore
    private Set<Theatre> theatres = new HashSet<>();

    Boolean isReleased = false;
    Boolean isPremiumEarly = false;

    public void setReleasedFlags() {
        if (this.getReleaseDatePublic().before(new Date())) {
            this.isReleased = true;
        }
        else {
            this.isReleased = false;
        }

        if (this.getReleaseDatePremium().before(new Date()) && this.getReleaseDatePublic().after(new Date())) {
            this.isPremiumEarly = true;
        }
        else {
            this.isPremiumEarly = false;
        }
    }

    public Movie() {}

    public Movie(String title, String synopsis, String duration, String ageRating, Date releaseDatePublic, Date releaseDatePremium, String genre, String posterFilePath, Boolean featured, Set<Showtime> showtimes, Set<Theatre> theatres) {
        this.title = title;
        this.synopsis = synopsis;
        this.duration = duration;
        this.ageRating = ageRating;
        this.releaseDatePublic = releaseDatePublic;
        this.releaseDatePremium = releaseDatePremium;
        this.genre = genre;
        this.posterFilePath = posterFilePath;
        this.featured = featured;
        this.showtimes = showtimes;
        this.theatres = theatres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(Set<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public Set<Theatre> getTheatres() {
        return theatres;
    }

    public void setTheatres(Set<Theatre> theatres) {
        this.theatres = theatres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public Date getReleaseDatePublic() {
        return releaseDatePublic;
    }

    public void setReleaseDatePublic(Date releaseDate) {
        this.releaseDatePublic = releaseDate;
    }

    public Date getReleaseDatePremium() {
        return releaseDatePremium;
    }

    public void setReleaseDatePremium(Date releaseDatePremium) {
        this.releaseDatePremium = releaseDatePremium;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPosterFilePath() {
        return posterFilePath;
    }

    public void setPosterFilePath(String posterFilePath) {
        this.posterFilePath = posterFilePath;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getPremiumEarly() {
        return isPremiumEarly;
    }

    public Boolean getReleased() {
        return isReleased;
    }
}
