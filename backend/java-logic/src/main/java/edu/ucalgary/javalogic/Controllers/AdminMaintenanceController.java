package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Entities.Movie;
import edu.ucalgary.javalogic.Entities.Showtime;
import edu.ucalgary.javalogic.Entities.Seat;
import edu.ucalgary.javalogic.repository.MovieRepository;
import edu.ucalgary.javalogic.repository.SeatRepository;
import edu.ucalgary.javalogic.repository.ShowtimeRepository;
import edu.ucalgary.javalogic.repository.TheatreRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import edu.ucalgary.javalogic.Entities.Theatre;

@RestController
@RequestMapping("api/admin")
@CrossOrigin(origins = "*") // Allow React app to connect
public class AdminMaintenanceController {

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    // THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE THEATRE
    @GetMapping("/theatres")
    public ResponseEntity<List<Theatre>> getTheatres(){return ResponseEntity.ok(theatreRepository.findAll());}

    @GetMapping("/theatres/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable Long id){
        return theatreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/theatres/{id}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable("id") Long id, @RequestBody Theatre theatreDetails){
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Theatre not found"));

        theatre.setName(theatreDetails.getName());
        theatre.setAddress(theatreDetails.getAddress());
        theatre.setNum_rows(theatreDetails.getNum_rows());
        theatre.setMax_seats_per_row(theatreDetails.getMax_seats_per_row());
        theatre.setMovies(theatreDetails.getMovies());

        return new ResponseEntity<>(theatreRepository.save(theatre), HttpStatus.OK);
    }

    @PostMapping("/theatres/add")
    public ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatreDetails){
        Theatre theatre = theatreRepository.save(new Theatre(theatreDetails.getName(),theatreDetails.getAddress(),theatreDetails.getNum_rows(), theatreDetails.getMax_seats_per_row()));
        return new ResponseEntity<>(theatre, HttpStatus.CREATED);
    }

    @DeleteMapping("/theatres/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id){
        if(theatreRepository.existsById(id)){
            theatreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    // MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE MOVIE

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(){return ResponseEntity.ok(movieRepository.findAll());}

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movieDetails){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + id));

        movie.setTitle(movieDetails.getTitle());
        movie.setSynopsis(movieDetails.getSynopsis());
        movie.setDuration(movieDetails.getDuration());
        movie.setAgeRating(movieDetails.getAgeRating());
        movie.setReleaseDatePublic(movieDetails.getReleaseDatePublic());
        movie.setReleaseDatePremium(movieDetails.getReleaseDatePremium());
        movie.setGenre(movieDetails.getGenre());
        movie.setPosterFilePath(movieDetails.getPosterFilePath());
        movie.setFeatured(movieDetails.getFeatured());

        return new ResponseEntity<>(movieRepository.save(movie), HttpStatus.OK);
    }

    @PostMapping("/movies/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movieDetails){
        Movie movie = movieRepository.save(
                new Movie(
                movieDetails.getTitle(),
                movieDetails.getSynopsis(),
                movieDetails.getDuration(),
                movieDetails.getAgeRating(),
                movieDetails.getReleaseDatePublic(),
                movieDetails.getReleaseDatePremium(),
                movieDetails.getGenre(),
                movieDetails.getPosterFilePath(),
                movieDetails.getFeatured(),
                movieDetails.getShowtimes(),
                movieDetails.getTheatres()));
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @DeleteMapping("/movies/{id}") // NOT working
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        if(movieRepository.existsById(id)){
            Movie movie = movieRepository.findById(id).get();
            List<Theatre> theatres = new ArrayList<>(movie.getTheatres());

            theatres.forEach(theatre -> theatre.removeMovie(movie));
            theatreRepository.saveAll(theatres);
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    // SHOWTIME SHOWTIME SHOWTIME SHOWTIME SHOWTIME SHOWTIME SHOWTIME SHOWTIME SHOWTIME
    @GetMapping("/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimes(){return ResponseEntity.ok(showtimeRepository.findAll());}

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id){
        return showtimeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable("id") Long id, @RequestBody Showtime showtimeDetails) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found"));
        showtime.set_Date(showtimeDetails.getDate());
        showtime.setMovie_id(showtimeDetails.getMovie_id());
        return new ResponseEntity<>(showtimeRepository.save(showtime), HttpStatus.OK);
    }

    @PostMapping("showtimes/add")
    public ResponseEntity<Showtime> addShowtime(@RequestBody Showtime showtimeDetails){
        Showtime showtime = showtimeRepository.save(new Showtime(showtimeDetails.getDate(), showtimeDetails.getMovie_id()));
        return new ResponseEntity<>(showtime, HttpStatus.CREATED);
    }

    @DeleteMapping("showtimes/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id){
        if(showtimeRepository.existsById(id)){
            showtimeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }



    // SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS SEATS
    @GetMapping("/seats")
    public ResponseEntity<List<Seat>> getSeats(){return ResponseEntity.ok(seatRepository.findAll());}

    @GetMapping("/seats/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id){
        return seatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/seats/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable("id") Long id, @RequestBody Seat seatDetails){
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
        seat.setSeat(seatDetails.getSeat());
        seat.setIsReserved(seatDetails.getIsReserved());
        seat.setShowtime_id(seatDetails.getShowtime_id());
        return new ResponseEntity<>(seatRepository.save(seat), HttpStatus.OK);
    }

    @PostMapping("/seats/add/showtime/{showtime_id}")
    public ResponseEntity<Seat> addSeat(@PathVariable("showtime_id") Long showtime_id, @RequestBody Seat seatDetails){
        Showtime showtime = showtimeRepository.findById(showtime_id).get();
        showtime.addSeat(seatDetails);
        Seat seat = seatRepository.save(new Seat(seatDetails.getSeat(), seatDetails.getIsReserved(), seatDetails.getShowtime_id()));
        return new ResponseEntity<>(seat, HttpStatus.CREATED);
    }

    @DeleteMapping("/seats/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id){
        if(seatRepository.existsById(id)){
            seatRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
