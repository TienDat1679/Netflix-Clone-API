package com.backend.controller;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.MailBody;
import com.backend.dto.request.UserCreationRequest;
import com.backend.dto.response.UserResponse;
import com.backend.entity.UserInfo;
import com.backend.repository.UserInfoRepository;
import com.backend.service.EmailService;
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
    EmailService emailService;
    UserInfoRepository userInfoRepository;

    @PostMapping
    public ApiResponse<UserResponse> registerUser(@Valid @RequestBody UserCreationRequest registerRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(registerRequest))
                .build();
    }

    @GetMapping("/verify/{otp}/{email}")
    public ResponseEntity<?> verifyUser(@PathVariable Integer otp, @PathVariable String email) {
        UserInfo user = userInfoRepository.findByEmail(email).orElse(null);

        if (user != null && user.getOtp().equals(otp)) {
            user.setEnabled(1);
            user.setOtp(null); // Clear OTP after successful verification
            userInfoRepository.save(user);
            return ResponseEntity.ok("Tài khoản đã được xác thực thành công.");
        } else {
            return ResponseEntity.badRequest().body("Mã xác thực không hợp lệ.");
        }
    }

    @GetMapping("/resend-otp/{email}")
    public ResponseEntity<?> resendOtp(@PathVariable String email) {
        UserInfo user = userInfoRepository.findByEmail(email).orElse(null);

        if (user != null) {
            int otp = otpGenerator();
            MailBody mailBody = MailBody.builder()
				.to(email)
				.text("This is the OTP for verify your Account request: " + otp)
				.subject("OTP for Verify Account request")
				.build();
            emailService.sendSimpleMessage(mailBody);

            user.setOtp(otp);
            userInfoRepository.save(user);
            return ResponseEntity.ok("Đã gửi lại mã OTP vào email của bạn !");
        } else {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }
    }


    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}