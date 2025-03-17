package com.backend.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Uncategorized error"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    USER_NOT_FOUND(1005, "User not found"),
    MOVIE_NOT_FOUND(1006, "Movie not found"),
    TV_SERIES_NOT_FOUND(1007, "TV Series not found"),
    INVALID_MEDIA_TYPE(1008, "Invalid media type"),
    MEDIA_NOT_FOUND(1009, "Media not found"),
    ;

    int code;
    String message;
}
