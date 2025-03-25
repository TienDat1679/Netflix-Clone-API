package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;

public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
    List<UserLike> findByUser(UserInfo user);
}
