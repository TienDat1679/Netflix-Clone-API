package com.backend.repository;

import java.util.List;
import java.util.Optional;

import com.backend.entity.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);
    
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);
    
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> searchMoviesByTitle(@Param("title") String title);

    @Query("SELECT DISTINCT m FROM Movie m " +
                "JOIN m.genres g " +
                "WHERE g.id IN (SELECT g2.id FROM Movie m2 JOIN m2.genres g2 WHERE m2.id = :movieId) " +
                "AND m.id <> :movieId")
    List<Movie> findMoviesWithSameGenres(@Param("movieId") Long movieId);

	@Query("SELECT m.trailers FROM Movie m WHERE m.id = :id")
    List<Trailer> findTrailersByMovieId(@Param("id") Long id);    // Lấy top 10 phim theo voteCount giảm dần
    List<Movie> findTop10ByOrderByVoteCountDesc();

    List<Movie> findTop5ByOrderByViewCountDesc();

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id IN :genreIds AND m.id <> :id")
    List<Movie> findMoviesByGenreIds(@Param("genreIds") List<Long> genreIds, @Param("id") Long id);

}
