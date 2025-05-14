package com.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.dto.MailBody;
import com.backend.dto.MediaDTO;
import com.backend.entity.MediaReminder;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.mapper.MediaMapper;
import com.backend.repository.MediaReminderRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.utils.UserUtil;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MediaReminderService {
    MediaReminderRepository reminderRepository;
    EmailService emailService;
    MovieRepository movieRepository;
    TVSerieRepository serieRepository;
    MediaService mediaService;
    UserInfoRepository userRepository;
    MediaMapper mediaMapper;

    @Scheduled(cron = "0 55 11 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailReminders() {
        String today = LocalDate.now().toString(); // yyyy-MM-dd

        List<Movie> movies = movieRepository.findByReleaseDate(today);
        List<TVSerie> series = serieRepository.findByFirstAirDate(today);

        List<MediaDTO> listMedia = new ArrayList<>();
        listMedia.addAll(mediaService.moviesToMediaDTOList(movies));
        listMedia.addAll(mediaService.tvSeriesToMediaDTOList(series));

        for (MediaDTO media : listMedia) {
            List<MediaReminder> reminders = reminderRepository.findByMediaIdAndNotifiedFalse(media.getId());
            for (MediaReminder reminder : reminders) {
                MailBody mailBody = MailBody.builder()
                        .to(reminder.getUser().getEmail())
                        .text("Hello! Just a reminder that " + media.getTitle() + " is released today. Enjoy watching!")
                        .subject("Reminder: " + media.getTitle() + " is released today!")
                        .build();
                emailService.sendSimpleMessage(mailBody);
                reminder.setNotified(true);
                reminderRepository.save(reminder);
            }
        }
    }

    public void createReminder(Long mediaId) {
        var user = userRepository.findByEmail(UserUtil.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var reminder = MediaReminder.builder()
                .mediaId(mediaId)
                .user(user)
                .notified(false)
                .createdAt(LocalDateTime.now())
                .build();
        reminderRepository.save(reminder);
    }

    public void deleteReminder(Long mediaId) {
        var user = userRepository.findByEmail(UserUtil.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var reminder = reminderRepository.findByMediaIdAndUserId(mediaId, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.REMINDER_NOT_FOUND));
        reminderRepository.delete(reminder);
    }

    public List<MediaDTO> getUserInbox() {
        var user = userRepository.findByEmail(UserUtil.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MediaReminder> reminders = reminderRepository.findByUserIdAndNotifiedTrue(user.getId());
        List<MediaDTO> mediaList = new ArrayList<>();
        for (MediaReminder reminder : reminders) {
            Movie movie = movieRepository.findById(reminder.getMediaId()).orElse(null);
            TVSerie serie = serieRepository.findById(reminder.getMediaId()).orElse(null);
            MediaDTO media = null;
            if (movie != null) {
                media = mediaMapper.toMediaDTO(movie);
            } else if (serie != null) {
                media = mediaMapper.toMediaDTO(serie);
            }
            mediaList.add(media);
        }
        return mediaList;
    }

    public List<MediaDTO> getMediaReminders() {
        var user = userRepository.findByEmail(UserUtil.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MediaReminder> reminders = reminderRepository.findByUserId(user.getId());
        List<MediaDTO> mediaList = new ArrayList<>();
        for (MediaReminder reminder : reminders) {
            Movie movie = movieRepository.findById(reminder.getMediaId()).orElse(null);
            TVSerie serie = serieRepository.findById(reminder.getMediaId()).orElse(null);
            MediaDTO media = null;
            if (movie != null) {
                media = mediaMapper.toMediaDTO(movie);
            } else if (serie != null) {
                media = mediaMapper.toMediaDTO(serie);
            }
            mediaList.add(media);
        }
        return mediaList;
    }
}
