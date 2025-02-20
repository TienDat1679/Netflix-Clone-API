package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.AuthRequest;
import com.backend.dto.AuthResponse;
import com.backend.dto.ErrorResponse;
import com.backend.repository.UserInfoRepository;
import com.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserInfoRepository userRepository;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            
            if (userRepository.findEnabledByEmail(authRequest.getEmail()) == 0) {
                return ResponseEntity.status(401).body(new ErrorResponse("Tài khoản chưa được kích hoạt"));
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
            final String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(417).body(new ErrorResponse("Tài khoản hoặc mật khẩu không hợp lệ"));
        }
    }
}
