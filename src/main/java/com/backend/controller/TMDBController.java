package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.service.TMDBService;

@RestController
@RequestMapping("/api/tmdb")
public class TMDBController {

    @Autowired
    private TMDBService tmdbService;

    @GetMapping("/fetch")
    public String fetchMovies() {
        tmdbService.fetchAndSaveMovies();
        return "Movies fetched and saved successfully!";
    }

    @GetMapping("/fetch-series")
    public String fetchSeries() {
        tmdbService.fetchAndSaveTVSeries();
        return "Series fetched and saved successfully!";
    }
}