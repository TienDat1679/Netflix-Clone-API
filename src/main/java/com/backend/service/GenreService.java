package com.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.entity.Genre;
import com.backend.model.GenreDTO;
import com.backend.repository.GenreRepository;

@Service
public class GenreService {
    
    @Autowired
    private GenreRepository genreRepository;

    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName(), genre.getDescription()))
                .collect(Collectors.toList());
    }
}
