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
