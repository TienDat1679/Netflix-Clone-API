package com.backend.service;

import java.util.Date;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.ChangePassword;
import com.backend.dto.MailBody;
import com.backend.entity.ForgotPassword;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.repository.ForgotPasswordRepository;
import com.backend.repository.UserInfoRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordService {
    UserInfoRepository userRepository;
    ForgotPasswordRepository forgotPasswordRepository;
    EmailService emailService;
    PasswordEncoder passwordEncoder;

    public String verifyEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		
        if (forgotPasswordRepository.existsByUser(user))
            forgotPasswordRepository.deleteByUser(user);
        
        Integer otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password request")
                .build();

		ForgotPassword fp = ForgotPassword.builder()
				.otp(otp)
				.expirationTime(new Date(System.currentTimeMillis() + 60 * 1000))
				.user(user)
				.build();
		
		emailService.sendSimpleMessage(mailBody);
		forgotPasswordRepository.save(fp);

        return otp.toString();
    }

    public boolean verifyOtp(Integer otp, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        if (fp.getExpirationTime().before(new Date())) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        return true;
    }

    public void changePassword(String email, ChangePassword changePassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!changePassword.password().equals(changePassword.repeatPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        // Delete the OTP record after successful password change
        forgotPasswordRepository.deleteByUser(user);
    }

    private Integer otpGenerator() {
		Random random = new Random();
		return random.nextInt(100_000, 999_999);
	}
}
