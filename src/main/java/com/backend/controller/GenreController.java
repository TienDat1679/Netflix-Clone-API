package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.GenreDTO;
import com.backend.entity.Genre;
import com.backend.repository.GenreRepository;
import com.backend.service.GenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @GetMapping("/movies")
    public ResponseEntity<List<Genre>> getAllGenresMovies() {
        List<Genre> genres = genreRepository.findAll();
        return ResponseEntity.ok(genres);
    }

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }
}
