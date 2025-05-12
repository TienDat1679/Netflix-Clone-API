package com.backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    String content;
    String userId;
    String userName;
    Long likes;
    Long mediaId;
    LocalDateTime createdAt;
    boolean likedByUser;
}
