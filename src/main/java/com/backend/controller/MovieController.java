package com.backend.controller;

import java.util.List;
import java.util.Optional;

import com.backend.entity.Trailer;
import com.backend.service.TrailerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Movie;
import com.backend.repository.MovieRepository;
import com.backend.service.MediaService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {
   MovieRepository movieRepository;
   MediaService mediaService;
   TrailerService trailerService;
   
   @GetMapping("/{genreId}")
   public List<?> getAllGenresMovies(@PathVariable Long genreId) {
      List<Movie> movies = movieRepository.findMoviesByGenreId(genreId);
      return mediaService.moviesToMediaDTOList(movies);
   }

   @GetMapping("")
   public ResponseEntity<?> getAllMovies() {

      List<Movie> movies = movieRepository.findAll();
      return ResponseEntity.ok(movies);
   }

   @GetMapping("/search")
   public ResponseEntity<?> searchMovies(@RequestParam("movieId") Long id) {

      Optional<Movie> movie = movieRepository.findById(id);
      return ResponseEntity.ok(movie);
   }

   @GetMapping("/search/movie-same")

   public ResponseEntity<?> searchMoviesWithSameGenres(@RequestParam("movieId") Long id) {

      List<Movie> movies = movieRepository.findMoviesByGenreId(id);
      if(movies.isEmpty())
      {
         movies = movieRepository.findAll();
      }
      return ResponseEntity.ok(movies);
   }

   @GetMapping("/top10")
   public List<Movie> getTop10Movies() {
        return movieRepository.findTop10ByOrderByVoteCountDesc();
   }

   @GetMapping("/trailer")
   public List<Trailer> getTrailersByMovieId(@RequestParam("id") Long movieId) {
      return trailerService.getTrailersMovie(movieId);
   }
   
}
