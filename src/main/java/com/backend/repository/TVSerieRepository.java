package com.backend.repository;

import java.util.List;
import java.util.Optional;

import com.backend.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.entity.TVSerie;

import jakarta.transaction.Transactional;

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

    @Query("SELECT t.episodes FROM TVSerie t WHERE t.id = :seriesId")
    List<Episode> findEpisodesByTVSerieId(@Param("seriesId") Long seriesId);

    @Query("SELECT t FROM TVSerie t JOIN t.genres g WHERE g.id IN :genreIds AND t.id <> :id")
    List<TVSerie> findTVSeriesByGenreIds(@Param("genreIds") List<Long> genreIds, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE TVSerie t SET t.voteCount = t.voteCount + 1 WHERE t.id = :id")
    void incrementVoteCount(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE TVSerie t SET t.voteCount = t.voteCount - 1 WHERE t.id = :id")
    void decrementVoteCount(@Param("id") Long id);

    @Query("SELECT s FROM TVSerie s JOIN s.genres g WHERE g.name IN :genreNames GROUP BY s.id HAVING COUNT(DISTINCT g.name) = :size")
    List<TVSerie> findTVSeriesByGenreNames(@Param("genreNames") List<String> genreNames, @Param("size") long size);

    List<TVSerie> findByFirstAirDate(String date);
}
