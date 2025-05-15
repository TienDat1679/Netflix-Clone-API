package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Long id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String releaseDate; 
    private String type; // "movie" hoặc "tv_series"
    private boolean isRemind;
    private int isPrenium;
}
