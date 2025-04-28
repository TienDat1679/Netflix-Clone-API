package com.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.entity.Trailer;
import com.backend.service.TrailerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/trailers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TrailerController {
    TrailerService trailerService;

    @GetMapping("/{mediaId}")
    ApiResponse<List<Trailer>> getTrailersByMediaId(@PathVariable Long mediaId) {
        return ApiResponse.<List<Trailer>>builder()
                .result(trailerService.getTrailersByMediaId(mediaId))
                .message("Trailers retrieved successfully")
                .build();
    }
}
