package com.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.request.CreateCommentRequest;
import com.backend.dto.response.CommentResponse;
import com.backend.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @GetMapping("/{mediaId}")
    ApiResponse<List<CommentResponse>> getCommentsByMediaId(@PathVariable Long mediaId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentsByMediaId(mediaId))
                .build();
    }

    @PostMapping
    ApiResponse<CommentResponse> createComment(@RequestBody CreateCommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @PostMapping("/{commentId}/like")
    ApiResponse<Void> likeComment(@PathVariable Long commentId) {
        commentService.likeComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Comment liked successfully")
                .build();
    }

    @PostMapping("/{commentId}/unlike")
    ApiResponse<Void> unlikeComment(@PathVariable Long commentId) {
        commentService.unlikeComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Comment unliked successfully")
                .build();
    }
}
