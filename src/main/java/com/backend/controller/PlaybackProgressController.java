package com.backend.controller;


import com.backend.dto.response.UserResponse;
import com.backend.entity.Movie;
import com.backend.entity.PlaybackProgress;
import com.backend.service.PlaybackProgressService;
import com.backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playback")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaybackProgressController {

    private PlaybackProgressService playbackProgressService;
    private UserService userService;


    @GetMapping("")
    public PlaybackProgress getProgress(
            @RequestParam("mediaId") Long mediaId) {
        // Lấy thông tin người dùng
        UserResponse user = userService.getMyInfo();
        String userId = user.getId();
        // Gọi service để lấy PlaybackProgress và trả về
        return playbackProgressService.getProgress(userId, mediaId);
    }
    @GetMapping("/user")
    public ResponseEntity<?> getPlaybackProgressByUserId() {
        UserResponse user = userService.getMyInfo();
        String userId = user.getId();
        List<PlaybackProgress> playbackProgressList = playbackProgressService.getProgressByUserId(userId);

        if (playbackProgressList != null && !playbackProgressList.isEmpty()) {
            System.out.println(playbackProgressList);
            return ResponseEntity.ok(playbackProgressList);
        } else {
            // Trả về ResponseEntity với mã trạng thái HTTP 404 (Not Found) nếu không tìm thấy tiến trình
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No playback progress found for user.");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveProgress(  @RequestParam("mediaId") Long mediaId,
                                                  @RequestParam("position") Long Positon) {
        UserResponse user = userService.getMyInfo();
        String userId = user.getId();
        System.out.println(userId);
        playbackProgressService.saveOrUpdateProgress(userId,mediaId,Positon);
        return ResponseEntity.ok("Progress saved successfully!");
    }
}
