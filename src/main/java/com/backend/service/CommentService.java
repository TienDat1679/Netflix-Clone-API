package com.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.dto.request.CreateCommentRequest;
import com.backend.dto.response.CommentResponse;
import com.backend.entity.Comment;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.mapper.CommentMapper;
import com.backend.repository.CommentRepository;
import com.backend.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    UserInfoRepository userRepository;
    CommentMapper commentMapper;

    public CommentResponse createComment(CreateCommentRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .mediaId(request.getMediaId())
                .likes(0L)
                .createdAt(LocalDateTime.now())
                .build();;
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public void likeComment(Long commentId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setLikes(comment.getLikes() + 1);
        comment.getLikedBy().add(userRepository.findByEmail(getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        commentRepository.save(comment);
    }

    public void unlikeComment(Long commentId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.getLikes() > 0) {
            comment.setLikes(comment.getLikes() - 1);
        }
        comment.getLikedBy().remove(userRepository.findByEmail(getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        commentRepository.save(comment);
    }

    public List<CommentResponse> getCommentsByMediaId(Long mediaId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByMediaIdOrderByLikesDescCreatedAtDesc(mediaId, pageable);
        return commentPage.stream()
                .map(comment -> {
                    CommentResponse response = commentMapper.toCommentResponse(comment);

                    // Kiểm tra xem người dùng đã like comment này chưa
                    boolean likedByUser = comment.getLikedBy().stream()
                            .anyMatch(user -> user.getEmail().equals(getUserEmail()));

                    response.setLikedByUser(likedByUser);
                    return response;
                })
                .collect(Collectors.toList());
    }

    private String getUserEmail() {
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
}
