// package com.backend.service;

// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.backend.entity.UserInfo;
// import com.backend.repository.UserInfoRepository;

// @Service
// public class UserInfoService implements UserDetailsService {
	
// 	@Autowired
// 	UserInfoRepository repository;

// 	@Override
// 	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
// 		Optional<UserInfo> userInfo = repository.findByEmail(email);
// 		return userInfo.map(UserInfoUserDetails::new)
// 				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
// 	}
// }
