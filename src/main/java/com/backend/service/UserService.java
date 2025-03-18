package com.backend.service;

import java.util.List;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.MailBody;
import com.backend.dto.MediaDTO;
import com.backend.dto.request.RegisterRequest;
import com.backend.dto.response.UserResponse;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;
import com.backend.entity.UserWatchList;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.mapper.MediaMapper;
import com.backend.mapper.UserMapper;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.repository.UserLikeRepository;
import com.backend.repository.UserWatchListRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	UserInfoRepository userRepository;
    MovieRepository movieRepository;
    TVSerieRepository tvSerieRepository;
    UserLikeRepository userLikeRepository;
    UserWatchListRepository userWatchlistRepository;
    MediaMapper mediaMapper;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailService emailService;

    public void likeMedia(String userId, Long mediaId, String type) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserLike userLike = new UserLike();
        userLike.setUser(user);

        if ("movie".equalsIgnoreCase(type)) {
            Movie movie = movieRepository.findById(mediaId)
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            userLike.setMovie(movie);
        } else if ("tv_series".equalsIgnoreCase(type)) {
            TVSerie tvSeries = tvSerieRepository.findById(mediaId)
                    .orElseThrow(() -> new AppException(ErrorCode.TV_SERIES_NOT_FOUND));
            userLike.setTvSerie(tvSeries);
        } else {
            throw new AppException(ErrorCode.INVALID_MEDIA_TYPE);
        }

        userLikeRepository.save(userLike);
    }

    public void addToWatchlist(String userId, Long mediaId, String type) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserWatchList watchlist = new UserWatchList();
        watchlist.setUser(user);

        if ("movie".equalsIgnoreCase(type)) {
            Movie movie = movieRepository.findById(mediaId)
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            watchlist.setMovie(movie);
        } else if ("tv_series".equalsIgnoreCase(type)) {
            TVSerie tvSeries = tvSerieRepository.findById(mediaId)
                    .orElseThrow(() -> new AppException(ErrorCode.TV_SERIES_NOT_FOUND));
            watchlist.setTvSerie(tvSeries);
        } else {
            throw new AppException(ErrorCode.INVALID_MEDIA_TYPE);
        }

        userWatchlistRepository.save(watchlist);
    }

    public List<MediaDTO> getLikedMedia(String userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserLike> likedItems = userLikeRepository.findByUser(user);
        return likedItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

    public List<MediaDTO> getWatchlistMedia(String userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserWatchList> watchlistItems = userWatchlistRepository.findByUser(user);
        return watchlistItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        
        UserInfo user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(request.getEmail())
                .text("This is the OTP for verify your Account request: " + otp)
                .subject("OTP for Verify Account request")
                .build();
        emailService.sendSimpleMessage(mailBody);
        user.setOtp(otp);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}