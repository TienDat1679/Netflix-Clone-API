package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entity.TVSerie;

@Repository
public interface TVSerieRepository extends JpaRepository<TVSerie, Long> {
    Optional<TVSerie> findByName(String name);
}
