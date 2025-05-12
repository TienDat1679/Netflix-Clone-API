package com.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.mapper.MediaMapper;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaService {
    MovieRepository movieRepository;
    TVSerieRepository tvSeriesRepository;
    MediaMapper mediaMapper;

    public List<MediaDTO> getTrendingMedia() {
        List<Movie> movies = movieRepository.findTop5ByOrderByViewCountDesc();
        List<TVSerie> tvSeries = tvSeriesRepository.findTop5ByOrderByViewCountDesc();

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        return results;
    }

    public List<MediaDTO> getMediaByGenre(Long genreId) {
        List<Movie> movies = movieRepository.findMoviesByGenreId(genreId);
        List<TVSerie> tvSeries = tvSeriesRepository.findTvSeriesByGenreId(genreId);

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        return results;
    }

    public List<MediaDTO> getTop10Series() {
        List<TVSerie> series = tvSeriesRepository.findTop10ByOrderByVoteCountDesc();
        return tvSeriesToMediaDTOList(series);
    }

    public List<MediaDTO> getTop10Movies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByVoteCountDesc();
        return moviesToMediaDTOList(movies);
    }

    public List<MediaDTO> searchMedia(String keyword) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(keyword);
        List<TVSerie> tvSeries = tvSeriesRepository.findByNameContainingIgnoreCase(keyword);

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        return results;
    }

    public List<MediaDTO> moviesToMediaDTOList(List<Movie> movies) {
        return movies.stream()
                .map(mediaMapper::toMediaDTO)
                .collect(Collectors.toList());
    }

    public List<MediaDTO> tvSeriesToMediaDTOList(List<TVSerie> tvSeriesList) {
        return tvSeriesList.stream()
                .map(mediaMapper::toMediaDTO)
                .collect(Collectors.toList());
    }

    public List<MediaDTO> searchMediaByGenres(List<String> genreNames) {
        List<Movie> movies = movieRepository.findMoviesByGenreNames(genreNames, genreNames.size());
        List<TVSerie> tvSeries = tvSeriesRepository.findTVSeriesByGenreNames(genreNames, genreNames.size());

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        return results;
    }
}
