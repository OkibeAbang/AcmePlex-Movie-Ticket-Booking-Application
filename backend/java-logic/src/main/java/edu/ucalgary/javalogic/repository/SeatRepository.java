package edu.ucalgary.javalogic.repository;

import edu.ucalgary.javalogic.Entities.Seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
//    List<Object> findBySeat(String seat);
//    Boolean existsBySeat(String seat);
//    Seat findSeatBySeat(String seat);
}