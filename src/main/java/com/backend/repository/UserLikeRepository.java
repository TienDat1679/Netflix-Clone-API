package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;

import jakarta.transaction.Transactional;

public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
    List<UserLike> findByUser(UserInfo user);
    boolean existsByUserAndMovie(UserInfo user, Movie movie);
    boolean existsByUserAndTvSerie(UserInfo user, TVSerie serie);
    @Transactional
    void deleteByUserAndMovie(UserInfo user, Movie movie);
    @Transactional
    void deleteByUserAndTvSerie(UserInfo user, TVSerie serie);
}
