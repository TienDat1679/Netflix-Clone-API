package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entity.MediaReminder;

@Repository
public interface MediaReminderRepository extends JpaRepository<MediaReminder, Long> {
    List<MediaReminder> findByMediaId(Long mediaId);
    List<MediaReminder> findByUserId(String userId);
    List<MediaReminder> findByMediaIdAndNotifiedFalse(Long mediaId);
    List<MediaReminder> findByUserIdAndNotifiedTrue(String userId);
    boolean existsByUserIdAndMediaId(String userId, Long mediaId);
    Optional<MediaReminder> findByMediaIdAndUserId(Long mediaId, String userId);
}
