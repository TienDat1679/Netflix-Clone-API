package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserWatchList;

import jakarta.transaction.Transactional;

public interface UserWatchListRepository extends JpaRepository<UserWatchList, Integer> {
    List<UserWatchList> findByUser(UserInfo user);
    boolean existsByUserAndMovie(UserInfo user, Movie movie);
    boolean existsByUserAndTvSerie(UserInfo user, TVSerie serie);
    @Transactional
    void deleteByUserAndMovie(UserInfo user, Movie movie);
    @Transactional
    void deleteByUserAndTvSerie(UserInfo user, TVSerie serie);
}
