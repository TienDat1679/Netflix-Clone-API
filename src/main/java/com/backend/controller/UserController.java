package com.backend.controller;

import java.util.List;

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
import com.backend.dto.request.AddToWatchListRequest;
import com.backend.dto.request.LikeRequest;
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

    @PostMapping("/{userId}/likes")
    ApiResponse<Void> likeMedia(@PathVariable String userId, @RequestBody LikeRequest request) {
        userService.likeMedia(userId, request);
        return ApiResponse.<Void>builder()
                .message("Like media successfully")
                .build();
    }   

    @PostMapping("/{userId}/watch-lists")
    ApiResponse<Void> addToWatchlist(@PathVariable String userId, @RequestBody AddToWatchListRequest request) {
        userService.addToWatchlist(userId, request);
        return ApiResponse.<Void>builder()
                .message("Add to watchlist successfully")
                .build();
    }

    @GetMapping("/{userId}/likes")
    ApiResponse<List<MediaDTO>> getLikedMedia(@PathVariable String userId) {
        return ApiResponse.<List<MediaDTO>>builder()
                .result(userService.getLikedMedia(userId))
                .build();
    }

    @GetMapping("/{userId}/watch-lists")
    ApiResponse<List<MediaDTO>> getWatchlistMedia(@PathVariable String userId) {
        return ApiResponse.<List<MediaDTO>>builder()
                .result(userService.getWatchlistMedia(userId))
                .build();
    }

    @DeleteMapping("/{userId}/likes/{mediaId}")
    ApiResponse<Void> unlikeMedia(@PathVariable String userId, @PathVariable Long mediaId) {
        userService.unlikeMedia(userId, mediaId);
        return ApiResponse.<Void>builder()
                .message("Unlike media successfully")
                .build();
    }

    @DeleteMapping("/{userId}/watch-lists/{mediaId}")
    ApiResponse<Void> removeFromWatchlist(@PathVariable String userId, @PathVariable Long mediaId) {
        userService.removeFromWatchlist(userId, mediaId);
        return ApiResponse.<Void>builder()
                .message("Remove from watchlist successfully")
                .build();
    }

    @GetMapping("/{userId}/likes/{mediaId}")
    ApiResponse<Boolean> isMediaLiked(@PathVariable String userId, @PathVariable Long mediaId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.isMediaLiked(userId, mediaId))
                .build();
    }

    @GetMapping("/{userId}/watch-lists/{mediaId}")
    ApiResponse<Boolean> isMediaInWatchlist(@PathVariable String userId, @PathVariable Long mediaId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.isMediaInWatchlist(userId, mediaId))
                .build();
    }
}
