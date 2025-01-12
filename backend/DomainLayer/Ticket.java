package DomainLayer;

public class Ticket {
    private Theatre theatre;
    private Movie movie;
    private Showtime showtime;
    private Seat seat;

    public Ticket(Theatre theatre, Movie movie, Showtime showtime, Seat seat) {
        this.theatre = theatre;
        this.movie = movie;
        this.showtime = showtime;
        this.seat = seat;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public Movie getMovie() {
        return movie;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }
}
