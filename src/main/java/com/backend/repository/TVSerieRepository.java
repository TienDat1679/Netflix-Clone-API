package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.entity.TVSerie;

@Repository
public interface TVSerieRepository extends JpaRepository<TVSerie, Long> {
    
    Optional<TVSerie> findByName(String name);
    
    @Query("SELECT t FROM TVSerie t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<TVSerie> searchTVSeriesByName(@Param("name") String name);

    @Query("SELECT t FROM TVSerie t JOIN t.genres g WHERE g.id = :genreId")
    List<TVSerie> findTvSeriesByGenreId(@Param("genreId") Long genreId);

    // Lấy top 10 phim theo voteCount giảm dần
    List<TVSerie> findTop10ByOrderByVoteCountDesc();

    List<TVSerie> findTop5ByOrderByViewCountDesc();

    List<TVSerie> findByNameContainingIgnoreCase(String name);
}
