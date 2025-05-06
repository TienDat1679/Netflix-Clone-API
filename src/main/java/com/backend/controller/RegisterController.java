package com.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.request.UserCreationRequest;
import com.backend.dto.response.UserResponse;
import com.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> registerUser(@Valid @RequestBody UserCreationRequest registerRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(registerRequest))
                .build();
    }

    @PostMapping("/verify/{otp}/{email}")
    public ApiResponse<UserResponse> verifyUser(@PathVariable Integer otp, @PathVariable String email) {
        return ApiResponse.<UserResponse>builder()
                .message("Verify user successfully")
                .result(userService.verifyUser(email, otp))
                .build();
    }

    @GetMapping("/resend-otp/{email}")
    public ApiResponse<String> resendOtp(@PathVariable String email) {
        return ApiResponse.<String>builder()
                .message("Resend OTP successfully")
                .result(userService.resendOtp(email))
                .build();
    }
}