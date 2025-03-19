// package com.backend.config;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.lang.NonNull;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.backend.util.JwtUtil;

// import io.jsonwebtoken.ExpiredJwtException;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class JwtRequestFilter extends OncePerRequestFilter {

//     @Autowired
//     private UserDetailsService userDetailsService;

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Override
//     protected void doFilterInternal(@NonNull HttpServletRequest request, 
//             @NonNull HttpServletResponse response, 
//             @NonNull FilterChain chain)
//             throws ServletException, IOException {

//         final String authorizationHeader = request.getHeader("Authorization");

//         String username = null;
//         String jwt = null;

//         try {
//             if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                 jwt = authorizationHeader.substring(7);
//                 username = jwtUtil.extractUsername(jwt);
//             }

//             if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                 UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//                 if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
//                     JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails,
//                             userDetails.getAuthorities());
//                     jwtAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                     SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
//                 }
//             }
//             chain.doFilter(request, response);

//         } catch (ExpiredJwtException e) {
//             // Trả về phản hồi khi JWT hết hạn
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.setContentType("application/json");
//             response.setCharacterEncoding("UTF-8");
//             response.getWriter().write("{\"error\": \"Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.\"}");
        
//         } catch (Exception e) {
//             // Xử lý các lỗi khác (ví dụ: JWT không hợp lệ)
//             response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//             response.setContentType("application/json");
//             response.setCharacterEncoding("UTF-8");
//             response.getWriter().write("{\"error\": \"Đã xảy ra lỗi khi xác thực.\"}");
//         }
//     }
// }