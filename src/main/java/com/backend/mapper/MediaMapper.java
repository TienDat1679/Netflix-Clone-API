package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    @Mapping(target = "type", constant = "movie")
    MediaDTO toMediaDTO(Movie movie);

    @Mapping(target = "title", source = "name")  // Chuyển name của TVSerie thành title
    @Mapping(target = "type", constant = "tv_series")
    MediaDTO toMediaDTO(TVSerie tvSeries);

    
}
