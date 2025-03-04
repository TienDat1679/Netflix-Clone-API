package com.backend.service;

import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.Trailer;
import com.backend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findMovieId(Long id) {
        return movieRepository.findById(id);
    }

    public List<Trailer> findTraillerMovies(Long movieId) {
        return  movieRepository.findTrailersByMovieId(movieId);
    }
}
