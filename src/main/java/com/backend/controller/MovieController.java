package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.TMDB.MovieDto;
import com.backend.service.TMDBService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private TMDBService tmdbService;

    // @GetMapping("/{id}")
    // public MovieDto getMovie(@PathVariable int id) {
    //     return tmdbService.getMovieDetails(id);
    // }

    // @GetMapping("/search")
    // public List<MovieDto> searchMovies(@RequestParam String query) {
    //     return tmdbService.searchMovies(query);
    // }

    // @GetMapping("/popular")
    // public List<MovieDto> getPopularMovies() {
    //     return tmdbService.getPopularMovies();
    // }
}
