package com.backend.service;

import com.backend.dto.response.UserResponse;
import com.backend.entity.PlaybackProgress;
import com.backend.repository.EpisodeRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.PlaybackProgressRepository;
import com.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaybackProgressService {

    @Autowired
    private PlaybackProgressRepository playbackProgressRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    private UserService userService;

    public PlaybackProgress getProgress(String userId, Long mediaId) {
        PlaybackProgress progress = null;
            progress = playbackProgressRepository.findByUserIdAndMediaId(userId, mediaId);
        return progress;  // Nếu progress là null, sẽ trả về null.
    }


    public void saveOrUpdateProgress(String userId, Long media, Long position) {

        PlaybackProgress existingProgress = null;
        existingProgress = playbackProgressRepository.findByUserIdAndMediaId(userId, media);

        // Nếu đã có thông tin, cập nhật
        if (existingProgress != null) {
            existingProgress.setPosition(position);
            playbackProgressRepository.save(existingProgress);
        } else {
            PlaybackProgress newProgress = new PlaybackProgress();
            newProgress.setUserId(userId);
            newProgress.setMediaId(media);
            newProgress.setPosition(position);
            // Nếu chưa có thông tin, lưu mới
            playbackProgressRepository.save(newProgress);
        }
    }

}

