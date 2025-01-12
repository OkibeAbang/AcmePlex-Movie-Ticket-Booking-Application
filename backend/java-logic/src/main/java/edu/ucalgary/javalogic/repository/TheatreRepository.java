package edu.ucalgary.javalogic.repository;

import edu.ucalgary.javalogic.Entities.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long>{
    Optional<Theatre> findTheatreByName(String theatreName);
    List<Theatre> findTheatresByMoviesId(Long movieId);
}
