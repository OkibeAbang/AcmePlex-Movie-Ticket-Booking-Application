package edu.ucalgary.javalogic.Entities;

import edu.ucalgary.javalogic.Entities.Seat;
import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "showtimes")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "_date", nullable = false)
    private Date _date;

    @Column(name = "movie_id", nullable = false)
    private Integer movie_id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "showtime_id")
    private Set<Seat> seats = new HashSet<>();

    public Showtime() {
    }

    public Showtime(Date date, Integer movie_id) {
        this._date = date;
        this.movie_id = movie_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return _date;
    }
    public Date get_Date() { return _date;}

    public void setDate(Date date) {
        this._date = date;
    }
    public void set_Date(Date date) { this._date = date;}

    public void setDate(String date) {
        this._date = convertDate(date);
    }

    private static Date convertDate(String dateTimeString) {
        // Define the date-time format
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setLenient(false); // Ensure strict parsing

        try {
            // Parse the string into a Date object
            return dateTimeFormat.parse(dateTimeString);
        } catch (ParseException e) {
            // Handle the exception if the string is not a valid date-time
            System.err.println("Invalid date-time format: " + dateTimeString);
            e.printStackTrace();
            return null; // Return null or handle as needed
        }
    }

    public String getShowingDateTimeString() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Format the Date object into the specified string format
        return dateTimeFormat.format(_date);
    }

    public String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(_date);
    }


    public String getTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(_date);
    }


    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

}

