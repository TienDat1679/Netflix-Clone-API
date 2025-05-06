package com.backend.repository;

import com.backend.entity.PlaybackProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaybackProgressRepository extends JpaRepository<PlaybackProgress, Integer> {

    PlaybackProgress findByUserIdAndMediaId(String userId, Long mediaId);
}
