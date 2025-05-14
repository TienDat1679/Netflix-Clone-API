package com.backend.controller;

import java.util.List;
import com.backend.service.ContentService;
import com.backend.service.MediaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.MediaDTO;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/media")
public class MediaController {
    ContentService contentService;
    MediaService mediaService;

    @GetMapping("/search")
    public List<MediaDTO> searchMedia(@RequestParam("keyword") String keyword) {
        return mediaService.searchMedia(keyword);
    }

    @GetMapping("/series/top10")
    public List<MediaDTO> getTop10Series() {
        return mediaService.getTop10Series();
    }

    @GetMapping("/movie/top10")
    public List<MediaDTO> getTop10Movies() {
        return mediaService.getTop10Movies();
    }

    @GetMapping("/{genreId}")
    public List<MediaDTO> getMediaByGenre(@PathVariable("genreId") Long id) {
        return mediaService.getMediaByGenre(id);
    }

    @GetMapping("/same")
    public List<MediaDTO> getMediaSame(@RequestParam("id") Long id) {
        return contentService.getSameMediaById(id);
    }

    @GetMapping("/trending")
    public List<MediaDTO> getTrendingMedia() {
        return mediaService.getTrendingMedia();
    }

    @GetMapping("/coming-soon")
    public List<MediaDTO> getComingSoonMedia() {
        return mediaService.getComingSoonMedia();
    }

    @GetMapping("/search-by-genres")
    ApiResponse<List<MediaDTO>> searchMediaByGenres(@RequestParam List<String> genres) {
        List<MediaDTO> media = mediaService.searchMediaByGenres(genres);
        return ApiResponse.<List<MediaDTO>>builder()
                .result(media)
                .build();
    }
}
