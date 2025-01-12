package edu.ucalgary.javalogic.repository;

import edu.ucalgary.javalogic.Entities.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    List<Movie> findMoviesByTheatresId(Long id);
    Boolean existsByTitle(String title);
    List<Movie> findMoviesByFeatured(Boolean featured);
    List<Movie> findByTitleContaining(String title);
}
