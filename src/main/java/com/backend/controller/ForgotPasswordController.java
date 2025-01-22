package com.backend.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.ForgotPassword;
import com.backend.entity.UserInfo;
import com.backend.model.ChangePassword;
import com.backend.model.MailBody;
import com.backend.repository.ForgotPasswordRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.service.EmailService;

@RestController
@RequestMapping("/api/forgotPassword")
public class ForgotPasswordController {

	private final UserInfoRepository userRepository;
	private final EmailService emailService;
	private final ForgotPasswordRepository forgotPasswordRepository;
	private final PasswordEncoder passwordEncoder;

	public ForgotPasswordController(UserInfoRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.forgotPasswordRepository = forgotPasswordRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// send mail for email verification
	@PostMapping("/verifyMail/{email}")
	public ResponseEntity<String> verifyEmail(@PathVariable String email) {
		UserInfo user = userRepository.findByEmail(email).orElse(null);
	
		if (user == null) {
			return ResponseEntity.badRequest().body("Email không tồn tại.");
		}

		int otp = otpGenerator();
		MailBody mailBody = MailBody.builder()
				.to(email)
				.text("This is the OTP for your Forgot Password request: " + otp)
				.subject("OTP for Forgot Password request")
				.build();

		ForgotPassword fp = ForgotPassword.builder()
				.otp(otp)
				.expirationTime(new Date(System.currentTimeMillis() + 30 * 1000))
				.user(user)
				.build();
		
		emailService.sendSimpleMessage(mailBody);
		forgotPasswordRepository.save(fp);
		
		return ResponseEntity.ok("Đã gửi mã OTP vào email của bạn !");
	}
	
	@PostMapping("/verifyOtp/{otp}/{email}")
	public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
		try {
			UserInfo user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("Vui lòng cung cấp một email hợp lệ!"));

			ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
					.orElseThrow(() -> new RuntimeException("Mã OTP không hợp lệ cho email: " + email));

			if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
				forgotPasswordRepository.deleteById(fp.getFpid());
				return new ResponseEntity<>("OTP đã hết hạn !", HttpStatus.EXPECTATION_FAILED);
			}

			return ResponseEntity.ok("Xác thực OTP thành công !");

		} catch (UsernameNotFoundException e) {
			return new ResponseEntity<>("Vui lòng cung cấp một email hợp lệ !", HttpStatus.EXPECTATION_FAILED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Mã OTP không hợp lệ !", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
														@PathVariable String email) {
		// if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
		// 	return new ResponseEntity<>("Hãy nhập lại mật khẩu !", HttpStatus.EXPECTATION_FAILED);
		// }
		
		String encodedPassword = passwordEncoder.encode(changePassword.password());
		userRepository.updatePassword(email, encodedPassword);
		
		return ResponseEntity.ok("Mật khẩu đổi thành công !");
	}
	
	@PostMapping("/resend-otp/{email}")
	public ResponseEntity<?> resendOtp(@PathVariable String email) {
		try {
			UserInfo user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("Vui lòng cung cấp một email hợp lệ!"));

			ForgotPassword fp = forgotPasswordRepository.findByUser(user)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy mã OTP cho email: " + email));

			int otp = otpGenerator();
			MailBody mailBody = MailBody.builder()
					.to(email)
					.text("This is the OTP for your Forgot Password request: " + otp)
					.subject("OTP for Forgot Password request")
					.build();

			fp.setOtp(otp);
			fp.setExpirationTime(new Date(System.currentTimeMillis() + 30 * 1000));
			forgotPasswordRepository.save(fp);

			emailService.sendSimpleMessage(mailBody);

			return ResponseEntity.ok("Đã gửi lại mã OTP vào email của bạn !");

		} catch (RuntimeException e) {
			UserInfo user = userRepository.findByEmail(email).orElse(null);

			int otp = otpGenerator();
			MailBody mailBody = MailBody.builder()
					.to(email)
					.text("This is the OTP for your Forgot Password request: " + otp)
					.subject("OTP for Forgot Password request")
					.build();

			ForgotPassword fp = ForgotPassword.builder()
					.otp(otp)
					.expirationTime(new Date(System.currentTimeMillis() + 30 * 1000))
					.user(user)
					.build();

			emailService.sendSimpleMessage(mailBody);
			forgotPasswordRepository.save(fp);

			return ResponseEntity.ok("Đã gửi lại mã OTP vào email của bạn !");
		}
	}

	private Integer otpGenerator() {
		Random random = new Random();
		return random.nextInt(100_000, 999_999);
	}
}
