package edu.ucalgary.javalogic.repository;

import edu.ucalgary.javalogic.Entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Instead of findByEarlyAccessTrue
    @Query("SELECT n FROM News n WHERE n.movie.releaseDatePremium < CURRENT_DATE AND n.movie.releaseDatePublic > CURRENT_DATE")
    List<News> findEarlyAccessNews();

    // For regular news
    @Query("SELECT n FROM News n WHERE n.movie.releaseDatePublic <= CURRENT_DATE")
    List<News> findPublicNews();
}
