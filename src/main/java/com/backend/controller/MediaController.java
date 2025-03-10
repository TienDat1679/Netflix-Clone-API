package com.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.backend.entity.Genre;
import com.backend.repository.GenreRepository;
import com.backend.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ContentService contentService;

    @GetMapping("/{genreId}")
    public List<MediaDTO> getMediaByGenre(@PathVariable Long genreId) {
        List<Movie> movies = movieRepository.findMoviesByGenreId(genreId);
        List<TVSerie> tvSeries = tvSeriesRepository.findTvSeriesByGenreId(genreId);

        List<MediaDTO> mediaList = new ArrayList<>();
        mediaList.addAll(MediaMapper.toMediaDTOList(movies));
        mediaList.addAll(MediaMapper.toMediaDTOListFromTvSeries(tvSeries));

        return mediaList;
    }
    @GetMapping("/same")
    public List<MediaDTO> getMediaSame(@RequestParam("id") Long id) {
            return contentService.getSameMediaById(id);
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
