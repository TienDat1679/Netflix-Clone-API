package com.backend.dto.TMDB;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MovieDto {
    private int id;
    private String title;
    private String overview;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("poster_path")
    private String posterPath;
    
    @JsonProperty("vote_average")
    private double rating;
}
