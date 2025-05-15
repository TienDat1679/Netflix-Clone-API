package com.backend.dto.TMDB;

import java.util.List;

import lombok.Data;

@Data
public class TMDbSearchTVResponse {
    private List<TVDto> results;
}
