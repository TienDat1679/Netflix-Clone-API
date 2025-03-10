package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entity.Trailer;

import java.util.List;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer, String> {

    List<Trailer> findByTvSerie_Id(Long seriesId);

    List<Trailer> findByMovie_Id(Long movieId);

}
