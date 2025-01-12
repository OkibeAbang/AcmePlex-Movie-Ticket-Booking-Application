package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Entities.Movie;
import edu.ucalgary.javalogic.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow React app to connect
public class SearchController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies/search/{title}")
    public ResponseEntity<List<Movie>> searchMovie(@PathVariable String title) {
        try {
            List<Movie> movies = movieRepository.findByTitleContaining(title);

            for (Movie movie : movies) {
                movie.setReleasedFlags();
            }

            return new ResponseEntity<>(movies, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
