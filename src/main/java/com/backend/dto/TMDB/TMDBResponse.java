package com.backend.dto.TMDB;

import java.util.List;

import com.backend.entity.Movie;

import lombok.Data;

@Data
public class TMDBResponse {
    private List<Movie> results;
}
