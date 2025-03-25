package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.UserInfo;
import com.backend.entity.UserWatchList;

public interface UserWatchListRepository extends JpaRepository<UserWatchList, Integer> {
    List<UserWatchList> findByUser(UserInfo user);
}
