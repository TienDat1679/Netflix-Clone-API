package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

}
