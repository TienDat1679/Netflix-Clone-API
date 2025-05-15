package com.backend.service;


import com.backend.dto.MediaDTO;
import com.backend.entity.Genre;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.repository.GenreRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ContentService {
    GenreRepository genreRepository;
    MovieRepository movieRepository;
    TVSerieRepository tvSerieRepository;
    MediaService mediaService;

    public List<MediaDTO> getSameMediaById(Long id) {
        List<Genre> genres;

        // Kiểm tra ID thuộc Movie hay TVSerie
        if (movieRepository.existsById(id)) {
            genres = genreRepository.findGenresByMovieId(id);
        } else if (tvSerieRepository.existsById(id)) {
            genres = genreRepository.findGenresByTVSerieId(id);
        } else {
            throw new EntityNotFoundException("Không tìm thấy nội dung với ID: " + id);
        }

        // Lấy danh sách ID thể loại
        List<Long> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        // Lấy danh sách Movie và TVSerie có cùng thể loại
        List<Movie> similarMovies = movieRepository.findMoviesByGenreIds(genreIds, id);
        List<TVSerie> similarSeries = tvSerieRepository.findTVSeriesByGenreIds(genreIds, id);

        List<MediaDTO> sameMedia = new ArrayList<>();
        sameMedia.addAll(mediaService.moviesToMediaDTOList(similarMovies));
        sameMedia.addAll(mediaService.tvSeriesToMediaDTOList(similarSeries));

        // Trả về danh sách tổng hợp
        return sameMedia;
    }
}

