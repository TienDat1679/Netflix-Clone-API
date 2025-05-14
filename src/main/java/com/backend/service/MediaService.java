package com.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.entity.Genre;
import com.backend.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.mapper.MediaMapper;
import com.backend.repository.MediaReminderRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.utils.UserUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaService {

    GenreRepository genreRepository;
    MovieRepository movieRepository;
    TVSerieRepository tvSeriesRepository;
    UserInfoRepository userRepository;
    MediaMapper mediaMapper;
    MediaReminderRepository reminderRepository;

    public List<MediaDTO> getTrendingMedia() {
        List<Movie> movies = movieRepository.findTop5ByOrderByViewCountDesc();
        List<TVSerie> tvSeries = tvSeriesRepository.findTop5ByOrderByViewCountDesc();

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        return results;
    }

    public List<MediaDTO> getComingSoonMedia() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate in30Days = today.plusDays(30);

        String startDate = today.format(formatter);
        String endDate = in30Days.format(formatter);

        List<Movie> movies = movieRepository.findMoviesReleasingBetween(startDate, endDate);
        List<TVSerie> tvSeries = tvSeriesRepository.findTvSeriesReleasingBetween(startDate, endDate);

        List<MediaDTO> results = new ArrayList<>();
        results.addAll(moviesToMediaDTOList(movies));
        results.addAll(tvSeriesToMediaDTOList(tvSeries));

        var user = userRepository.findByEmail(UserUtil.getUserEmail()).orElse(null);

        if (user != null) {
            for (MediaDTO media : results) {
                if (reminderRepository.existsByUserIdAndMediaId(user.getId(), media.getId())) {
                    media.setRemind(true);
                }
            }
        }

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
        // Tìm các bộ phim theo tiêu đề
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(keyword);

        // Tìm các TV series theo tên
        List<TVSerie> tvSeries = tvSeriesRepository.findByNameContainingIgnoreCase(keyword);

        // Tìm các thể loại theo tên
        List<Genre> genres = genreRepository.findByNameContainingIgnoreCase(keyword);

        // Nếu có các thể loại, tìm tất cả các bộ phim và series thuộc các thể loại này
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                // Lấy tất cả các bộ phim thuộc thể loại
                movies.addAll(genre.getMovies());

                // Lấy tất cả các TV series thuộc thể loại
                tvSeries.addAll(genre.getSeries());
            }
        }

        // Chuyển các đối tượng Movie và TVSeries thành MediaDTO và trả về kết quả
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
