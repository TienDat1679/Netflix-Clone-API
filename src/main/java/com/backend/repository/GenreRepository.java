package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.entity.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.id = :id")
    List<Genre> findGenresByMovieId(@Param("id") Long id);

    @Query("SELECT g FROM Genre g JOIN g.series s WHERE s.id = :id")
    List<Genre> findGenresByTVSerieId(@Param("id") Long id);

    @Query("SELECT g FROM Genre g WHERE LOWER(g.name) LIKE %:keyword%")
    List<Genre> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

}
