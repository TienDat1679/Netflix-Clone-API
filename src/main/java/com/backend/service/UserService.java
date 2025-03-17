package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;
import com.backend.entity.UserWatchList;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.mapper.MediaMapper;
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

    public void likeMedia(int userId, Long mediaId, String type) {
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

    public void addToWatchlist(int userId, Long mediaId, String type) {
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

    public List<MediaDTO> getLikedMedia(int userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserLike> likedItems = userLikeRepository.findByUser(user);
        return likedItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

    public List<MediaDTO> getWatchlistMedia(int userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserWatchList> watchlistItems = userWatchlistRepository.findByUser(user);
        return watchlistItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

	public boolean existsByEmail(String email) {
        UserInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user != null;
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }
}