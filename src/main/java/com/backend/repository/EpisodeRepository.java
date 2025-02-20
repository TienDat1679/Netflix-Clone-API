package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entity.Episode;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

}
