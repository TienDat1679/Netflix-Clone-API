package com.backend.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.UserInfo;
import com.backend.model.AuthRequest;
import com.backend.model.AuthResponse;
import com.backend.model.MailBody;
import com.backend.model.RegisterRequest;
import com.backend.repository.UserInfoRepository;
import com.backend.service.EmailService;
import com.backend.service.UserService;
import com.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            final String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Tài khoản hoặc mật khẩu không hợp lệ");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Kiểm tra xem user đã tồn tại chưa
            if (userService.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body("Người dùng đã tồn tại.");
            }

            // Tạo tài khoản mới
            UserInfo user = new UserInfo();
            user.setName(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRoles("ROLE_USER");
            user.setEnabled(0); // Tài khoản chưa được kích hoạt

            int otp = otpGenerator();
            MailBody mailBody = MailBody.builder()
				.to(registerRequest.getEmail())
				.text("This is the OTP for verify your Account request: " + otp)
				.subject("OTP for Verify Account request")
				.build();
            emailService.sendSimpleMessage(mailBody);

            user.setOtp(otp);
            userService.save(user);

            return ResponseEntity.ok("Đăng ký thành công. Vui lòng kiểm tra email để kích hoạt tài khoản.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đăng ký thất bại. Vui lòng thử lại.");
        }
    }

    @GetMapping("/register/verify/{otp}/{email}")
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

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}