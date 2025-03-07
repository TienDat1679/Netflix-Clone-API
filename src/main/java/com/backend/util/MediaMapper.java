package com.backend.util;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;

public class MediaMapper {
    // Chuyển đổi từ Movie -> MediaDTO
    public static MediaDTO toMediaDTO(Movie movie) {
        return new MediaDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getBackdropPath(),
                "movie"
        );
    }

    // Chuyển đổi từ TvSeries -> MediaDTO
    public static MediaDTO toMediaDTO(TVSerie tvSeries) {
        return new MediaDTO(
                tvSeries.getId(),
                tvSeries.getName(), 
                tvSeries.getOverview(),
                tvSeries.getPosterPath(),
                tvSeries.getBackdropPath(),
                "tv_series"
        );
    }

    // Chuyển đổi danh sách Movie -> List<MediaDTO>
    public static List<MediaDTO> toMediaDTOList(List<Movie> movies) {
        return movies.stream().map(MediaMapper::toMediaDTO).collect(Collectors.toList());
    }

    // Chuyển đổi danh sách TvSeries -> List<MediaDTO>
    public static List<MediaDTO> toMediaDTOListFromTvSeries(List<TVSerie> tvSeriesList) {
        return tvSeriesList.stream().map(MediaMapper::toMediaDTO).collect(Collectors.toList());
    }
}
