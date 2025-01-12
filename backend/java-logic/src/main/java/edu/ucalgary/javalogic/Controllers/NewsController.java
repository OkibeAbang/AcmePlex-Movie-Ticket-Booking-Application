package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Entities.Movie;
import edu.ucalgary.javalogic.Entities.News;
import edu.ucalgary.javalogic.repository.NewsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/news")
@CrossOrigin(origins = "*") // Allow React app to connect
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    // Create a new news item
    @PostMapping("/create")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News savedNews = newsRepository.save(news);
        return ResponseEntity.ok(savedNews);
    }

    @GetMapping("/early_access")
    public ResponseEntity<List<News>> getEarlyAccessNews() {
        List<News> newsList = newsRepository.findEarlyAccessNews();
        // earlyAccess flag will be set in your existing code
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/public")
    public ResponseEntity<List<News>> getPublicNews() {
        List<News> newsList = newsRepository.findPublicNews();
        return ResponseEntity.ok(newsList);
    }
}
