package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.MediaDTO;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/like/{mediaType}/{mediaId}")
    public ResponseEntity<String> likeMedia(@PathVariable int userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId) {
        userService.likeMedia(userId, mediaId, mediaType);
        return ResponseEntity.ok("Liked " + mediaType + " successfully");
    }

    @PostMapping("/{userId}/watchlist/{mediaType}/{mediaId}")
    public ResponseEntity<String> addToWatchlist(@PathVariable int userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId) {
        userService.addToWatchlist(userId, mediaId, mediaType);
        return ResponseEntity.ok("Added " + mediaType + " to watchlist successfully");
    }

    @GetMapping("/{userId}/likes")
    public List<MediaDTO> getLikedMedia(@PathVariable int userId) {
        return userService.getLikedMedia(userId);
    }

    @GetMapping("/{userId}/watchlist")
    public List<MediaDTO> getWatchlistMedia(@PathVariable int userId) {
        return userService.getWatchlistMedia(userId);
    }
}
