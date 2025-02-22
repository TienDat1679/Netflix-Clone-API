package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.TMDB.MovieDto;
import com.backend.entity.Genre;
import com.backend.entity.Movie;
import com.backend.repository.GenreRepository;
import com.backend.repository.MovieRepository;
import com.backend.service.GenreService;
import com.backend.service.TMDBService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

   @Autowired
   private MovieRepository movieRepo;
   
   @Autowired
   private GenreRepository genreRepository;
  
   
   @Autowired
   private GenreService genreService;
   
   
   @GetMapping("/{genreId}")
   public ResponseEntity<?> getAllGenresMovies(@PathVariable("genreId") Long id) {
       List<Movie> movies = movieRepo.findMoviesByGenreId(id);
       return ResponseEntity.ok(movies);
   }

   @GetMapping("")
   public ResponseEntity<?>getMovies(@RequestParam("title") String title){
	   return ResponseEntity.ok(null);
   }

}
