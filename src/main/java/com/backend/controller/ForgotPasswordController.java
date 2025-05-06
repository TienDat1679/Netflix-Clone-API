package com.backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.ChangePassword;
import com.backend.service.ForgotPasswordService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/forgotPassword")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordController {
	ForgotPasswordService forgotPasswordService;

	@PostMapping("/verifyMail/{email}")
	public ApiResponse<String> verifyEmail(@PathVariable String email) {
		return ApiResponse.<String>builder()
				.result(forgotPasswordService.verifyEmail(email))
				.build(); 
	}
	
	@PostMapping("/verifyOtp/{otp}/{email}")
	public ApiResponse<Boolean> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
		return ApiResponse.<Boolean>builder()
				.result(forgotPasswordService.verifyOtp(otp, email))
				.build();
	}
	
	@PostMapping("/changePassword/{email}")
	public ApiResponse<Void> changePasswordHandler(@RequestBody ChangePassword changePassword,
			@PathVariable String email) {
		forgotPasswordService.changePassword(email, changePassword);
		return ApiResponse.<Void>builder()
				.message("Password changed successfully")
				.build();
	}
}
