package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
