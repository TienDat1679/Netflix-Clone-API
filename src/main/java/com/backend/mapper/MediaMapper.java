package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserLike;
import com.backend.entity.UserWatchList;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    
    @Mapping(target = "type", constant = "movie")
    MediaDTO toMediaDTO(Movie movie);

    @Mapping(target = "title", source = "name")  // Chuyển name của TVSerie thành title
    @Mapping(target = "type", constant = "tv_series")
    MediaDTO toMediaDTO(TVSerie tvSeries);

    default MediaDTO toMediaDTO(UserLike like) {
        if (like.getMovie() != null) {
            return toMediaDTO(like.getMovie());
        } else if (like.getTvSerie() != null) {
            return toMediaDTO(like.getTvSerie());
        }
        return null; // Trường hợp không có dữ liệu
    }

    default MediaDTO toMediaDTO(UserWatchList watchList) {
        if (watchList.getMovie() != null) {
            return toMediaDTO(watchList.getMovie());
        } else if (watchList.getTvSerie() != null) {
            return toMediaDTO(watchList.getTvSerie());
        }
        return null; // Trường hợp không có dữ liệu
    }
}
