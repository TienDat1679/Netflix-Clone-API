package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.MediaDTO;
import com.backend.dto.request.UserUpdateRequest;
import com.backend.dto.response.UserResponse;
import com.backend.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities()
                .forEach(authority -> log.info("Authority: {}", authority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, 
            @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

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
