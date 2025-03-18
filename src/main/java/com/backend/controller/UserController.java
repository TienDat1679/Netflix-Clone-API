package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.MediaDTO;
import com.backend.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/{userId}/like/{mediaType}/{mediaId}")
    public ResponseEntity<String> likeMedia(@PathVariable String userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId) {
        userService.likeMedia(userId, mediaId, mediaType);
        return ResponseEntity.ok("Liked " + mediaType + " successfully");
    }   

    @PostMapping("/{userId}/watchlist/{mediaType}/{mediaId}")
    public ResponseEntity<String> addToWatchlist(@PathVariable String userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId) {
        userService.addToWatchlist(userId, mediaId, mediaType);
        return ResponseEntity.ok("Added " + mediaType + " to watchlist successfully");
    }

    @GetMapping("/{userId}/likes")
    public List<MediaDTO> getLikedMedia(@PathVariable String userId) {
        return userService.getLikedMedia(userId);
    }

    @GetMapping("/{userId}/watchlist")
    public List<MediaDTO> getWatchlistMedia(@PathVariable String userId) {
        return userService.getWatchlistMedia(userId);
    }
}
