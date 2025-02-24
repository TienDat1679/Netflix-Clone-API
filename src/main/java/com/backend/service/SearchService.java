package com.backend.service;

import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TVSerieRepository tvSerieRepository;

    public Map<String, Object> searchByTitleOrName(String keyword) {
        List<Movie> movies = movieRepository.searchMoviesByTitle(keyword);
        List<TVSerie> tvSeries = tvSerieRepository.searchTVSeriesByName(keyword);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("tvSeries", tvSeries);

        return response;
    }
}
