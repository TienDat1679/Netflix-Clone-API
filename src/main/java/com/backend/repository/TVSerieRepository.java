package com.backend.repository;

import java.util.List;
import java.util.Optional;

import com.backend.entity.Episode;
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

    // Lấy top 10 phim theo voteCount giảm dần
    List<TVSerie> findTop10ByOrderByVoteCountDesc();

    List<TVSerie> findTop5ByOrderByViewCountDesc();

    @Query("SELECT t.episodes FROM TVSerie t WHERE t.id = :seriesId")
    List<Episode> findEpisodesByTVSerieId(@Param("seriesId") Long seriesId);


}
