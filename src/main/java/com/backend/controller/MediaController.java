package com.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.util.MediaMapper;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TVSerieRepository tvSeriesRepository;

    @GetMapping("/search")
    public List<MediaDTO> searchMedia(@RequestParam String keyword) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(keyword);
        List<TVSerie> tvSeries = tvSeriesRepository.findByNameContainingIgnoreCase(keyword);

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(MediaMapper.toMediaDTOListFromTvSeries(tvSeries));
        results.addAll(MediaMapper.toMediaDTOList(movies));

        return results;
    }

    @GetMapping("/series/top10")
    public List<MediaDTO> getTop10Series() {
        List<TVSerie> series = tvSeriesRepository.findTop10ByOrderByVoteCountDesc();
        List<MediaDTO> mediaList = new ArrayList<>();
        mediaList.addAll(MediaMapper.toMediaDTOListFromTvSeries(series));
        return mediaList;
    }

    @GetMapping("/movie/top10")
    public List<MediaDTO> getTop10Movies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByVoteCountDesc();
        List<MediaDTO> mediaList = new ArrayList<>();
        mediaList.addAll(MediaMapper.toMediaDTOList(movies));
        return mediaList;
    }

    @GetMapping("/{genreId}")
    public List<MediaDTO> getMediaByGenre(@PathVariable Long genreId) {
        List<Movie> movies = movieRepository.findMoviesByGenreId(genreId);
        List<TVSerie> tvSeries = tvSeriesRepository.findTvSeriesByGenreId(genreId);

        List<MediaDTO> mediaList = new ArrayList<>();
        mediaList.addAll(MediaMapper.toMediaDTOList(movies));
        mediaList.addAll(MediaMapper.toMediaDTOListFromTvSeries(tvSeries));

        return mediaList;
    }

    @GetMapping("/trending")
    public List<MediaDTO> getTrendingMedia() {
        List<Movie> topMovies = movieRepository.findTop5ByOrderByViewCountDesc();
        List<TVSerie> topTvSeries = tvSeriesRepository.findTop5ByOrderByViewCountDesc();

        List<MediaDTO> trendingMedia = new ArrayList<>();
        trendingMedia.addAll(MediaMapper.toMediaDTOList(topMovies));
        trendingMedia.addAll(MediaMapper.toMediaDTOListFromTvSeries(topTvSeries));

        // Trả về danh sách tổng hợp
        return trendingMedia;
    }
}
