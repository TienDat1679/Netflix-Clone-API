package com.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.MediaDTO;
import com.backend.service.MediaReminderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reminders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaReminderController {
    MediaReminderService mediaReminderService;

    @PostMapping("/{mediaId}")
    ApiResponse<Void> createReminder(@PathVariable Long mediaId) {
        mediaReminderService.createReminder(mediaId);
        return ApiResponse.<Void>builder()
                .message("Create reminder successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<MediaDTO>> getUserInbox() {
        return ApiResponse.<List<MediaDTO>>builder()
                .result(mediaReminderService.getUserInbox())
                .build();
    }
}
