package com.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TVSerieRepository tvSeriesRepository;

    @GetMapping("/trending")
    public List<MediaDTO> getTrendingMedia() {
        List<Movie> topMovies = movieRepository.findTop5ByOrderByViewCountDesc();
        List<TVSerie> topTvSeries = tvSeriesRepository.findTop5ByOrderByViewCountDesc();

        List<MediaDTO> trendingMedia = new ArrayList<>();

        // Chuyển đổi Movie -> MediaDTO
        for (Movie movie : topMovies) {
            trendingMedia.add(new MediaDTO(
                    movie.getId(),
                    movie.getTitle(), // Dùng title cho Movie
                    movie.getOverview(),
                    movie.getPosterPath(),
                    movie.getBackdropPath(),
                    "movie"));
        }

        // Chuyển đổi TvSeries -> MediaDTO
        for (TVSerie series : topTvSeries) {
            trendingMedia.add(new MediaDTO(
                    series.getId(),
                    series.getName(), // Dùng name cho TvSeries
                    series.getOverview(),
                    series.getPosterPath(),
                    series.getBackdropPath(),
                    "tv_series"));
        }

        // Trả về danh sách tổng hợp
        return trendingMedia;
    }
}
