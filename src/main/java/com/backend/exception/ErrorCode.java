package com.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key in validation", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1005, "Invalid email", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    MOVIE_NOT_FOUND(1007, "Movie not found", HttpStatus.NOT_FOUND),
    TV_SERIES_NOT_FOUND(1008, "TV Series not found", HttpStatus.NOT_FOUND),
    INVALID_MEDIA_TYPE(1009, "Invalid media type", HttpStatus.BAD_REQUEST),
    MEDIA_NOT_FOUND(1010, "Media not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1011, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1012, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1013, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVATED(1014, "Account not activated", HttpStatus.UNAUTHORIZED),
    ;

    int code;
    String message;
    HttpStatus statusCode;
}
