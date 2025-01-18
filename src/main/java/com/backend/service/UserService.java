package com.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.entity.UserInfo;
import com.backend.repository.UserInfoRepository;

@Service
public class UserService {
	
    @Autowired
    private UserInfoRepository userRepository;

	public boolean existsByEmail(String email) {
        UserInfo user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }

    
}