package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Entities.*;
import edu.ucalgary.javalogic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow React app to connect
public class ViewController {

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/theatres")
    public ResponseEntity<List<Theatre>> getTheatres() {
        try {
            List<Theatre> theatres = theatreRepository.findAll();
            return ResponseEntity.ok(theatres);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/theatres/{id}/movies")
    public ResponseEntity<List<Movie>> getMoviesByTheatreId(@PathVariable long id) {
        if (!theatreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Movie> movies = movieRepository.findMoviesByTheatresId(id);
        for (Movie movie : movies) {
            movie.setReleasedFlags();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        try {
            List<Movie> movies = movieRepository.findAll();
            for (Movie movie : movies) {
                movie.setReleasedFlags();
            }
            return ResponseEntity.ok(movies);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movies/featured")
    public ResponseEntity<List<Movie>> getFeaturedMovies() {
        try {
            List<Movie> movies = movieRepository.findMoviesByFeatured(Boolean.TRUE);
            for (Movie movie : movies) {
                movie.setReleasedFlags();
            }
            return ResponseEntity.ok(movies);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movies/free")
    public ResponseEntity<List<Movie>> getFreeMovies() {
        try {
            List<Movie> movies = movieRepository.findAll();
            List<Movie> freeMovies = new ArrayList<>();
            for (Movie movie : movies) {
                if (movie.getReleaseDatePublic().before(new Date())) {
                    freeMovies.add(movie);
                    movie.setReleasedFlags();
                }
            }
            return ResponseEntity.ok(freeMovies);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movies/premium")
    public ResponseEntity<List<Movie>> getPremiumMovies() {
        try {
            List<Movie> movies = movieRepository.findMoviesByFeatured(Boolean.TRUE);
            List<Movie> premiumMovies = new ArrayList<>();
            for (Movie movie : movies) {
                if (movie.getReleaseDatePremium().before(new Date()) && movie.getReleaseDatePublic().after(new Date())) {
//                    System.out.println(movie.getTitle());
                    premiumMovies.add(movie);
                    movie.setReleasedFlags();
                }
            }
            return ResponseEntity.ok(premiumMovies);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movies/title/{title}")
    public ResponseEntity<Movie> getMovieByTitle(@PathVariable String title) {
        try {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new RuntimeException("Movie not found: " + title));
            movie.setReleasedFlags();
            return ResponseEntity.ok(movie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movies/id/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found:" + id);
        }
        Movie movie = movieRepository.findById(id).get();
        movie.setReleasedFlags();
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/movies/id/{id}/theatres")
    public ResponseEntity<List<Theatre>> getTheatresByMovieId(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found:" + id);
        }

        List<Theatre> theatres = theatreRepository.findTheatresByMoviesId(id);
        return new ResponseEntity<>(theatres, HttpStatus.OK);
    }

    @GetMapping("/movies/id/{movie_id}/{theatre_id}/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieAndTheatre(@PathVariable Long movie_id, @PathVariable Long theatre_id) {
        if (!movieRepository.existsById(movie_id)) {
            throw new RuntimeException("Movie not found:" + movie_id);
        } else if (!theatreRepository.existsById(theatre_id)) {
            throw new RuntimeException("Theatre not found:" + theatre_id);
        }

        Theatre theatre = theatreRepository.findById(theatre_id).get();
        Movie movie = movieRepository.findById(movie_id).get();
        if (!theatre.getMovies().contains(movie)) {
            throw new RuntimeException("Movie: " + movie_id + "not showing at theatre:" + theatre_id);
        }

        LocalDateTime now = LocalDateTime.now();
        List<Showtime> showtimes = movie.getShowtimes().stream()
                .filter(showtime -> showtime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(now))
                .collect(Collectors.toList());

        return new ResponseEntity<>(showtimes, HttpStatus.OK);
    }

    @GetMapping("/movies/title/{title}/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieTitle(@PathVariable(value = "title") String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + title));
        List<Showtime> showtimes = new ArrayList<>(movie.getShowtimes());
        return new ResponseEntity<>(showtimes, HttpStatus.OK);
    }

    @GetMapping("/seats")
    public ResponseEntity<List<Seat>> getAllSeats() {
        try {
            List<Seat> seats = seatRepository.findAll();
            return ResponseEntity.ok(seats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable(value = "id") Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime not found:" + id);
        }
        Showtime showtime = showtimeRepository.findById(id).get();
        return ResponseEntity.ok(showtime);
    }

    @GetMapping("/showtimes/{showtime_id}/seats")
    public ResponseEntity<List<Seat>> getSeatsByShowtimeId(@PathVariable(value = "showtime_id") Integer showtime_id) {
        Showtime showtime = showtimeRepository.findById(showtime_id)
                .orElseThrow(() -> new RuntimeException("Seats for showtime with showtime_id:" + showtime_id + " not found"));
        List<Seat> seats = new ArrayList<>();
        seats.addAll(showtime.getSeats());
        return new ResponseEntity<>(seats, HttpStatus.OK);

    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable(value = "id") Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}/tickets")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        List<Ticket> tickets = user.getTickets();
        LocalDateTime now = LocalDateTime.now();

        // Update expired tickets
        tickets.forEach(ticket -> {
            Showtime showtime = ticket.getShowtime();
            if (showtime.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .isBefore(now) &&
                    ticket.getState().equals("Reserved")) {

                ticket.setState("Expired");
                ticketRepository.save(ticket);
            }
        });

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/payment_methods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethodsByUserId(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.addAll(user.getPayment_methods());
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }

}

