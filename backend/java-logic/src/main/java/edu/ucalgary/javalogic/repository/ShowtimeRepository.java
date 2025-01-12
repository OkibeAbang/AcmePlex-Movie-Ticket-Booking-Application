package edu.ucalgary.javalogic.repository;

import edu.ucalgary.javalogic.Entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    Optional<Showtime> findById(Integer id);
}
