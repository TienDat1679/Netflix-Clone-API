package com.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.MediaDTO;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;
import com.backend.entity.UserWatchList;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.repository.UserLikeRepository;
import com.backend.repository.UserWatchListRepository;

@Service
public class UserService {
	@Autowired
    private UserInfoRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TVSerieRepository tvSerieRepository;
    @Autowired
    private UserLikeRepository userLikeRepository;
    @Autowired
    private UserWatchListRepository userWatchlistRepository;

    public void likeMedia(int userId, Long mediaId, String type) {
        UserInfo user = userRepository.findById(userId).orElseThrow();

        UserLike userLike = new UserLike();
        userLike.setUser(user);

        if ("movie".equalsIgnoreCase(type)) {
            Movie movie = movieRepository.findById(mediaId).orElseThrow();
            userLike.setMovie(movie);
        } else if ("tv_series".equalsIgnoreCase(type)) {
            TVSerie tvSeries = tvSerieRepository.findById(mediaId).orElseThrow();
            userLike.setTvSerie(tvSeries);
        } else {
            throw new IllegalArgumentException("Invalid media type: " + type);
        }

        userLikeRepository.save(userLike);
    }

    public void addToWatchlist(int userId, Long mediaId, String type) {
        UserInfo user = userRepository.findById(userId).orElseThrow();

        UserWatchList watchlist = new UserWatchList();
        watchlist.setUser(user);

        if ("movie".equalsIgnoreCase(type)) {
            Movie movie = movieRepository.findById(mediaId).orElseThrow();
            watchlist.setMovie(movie);
        } else if ("tv_series".equalsIgnoreCase(type)) {
            TVSerie tvSeries = tvSerieRepository.findById(mediaId).orElseThrow();
            watchlist.setTvSerie(tvSeries);
        } else {
            throw new IllegalArgumentException("Invalid media type: " + type);
        }

        userWatchlistRepository.save(watchlist);
    }

    public List<MediaDTO> getLikedMedia(int userId) {
        UserInfo user = userRepository.findById(userId).orElseThrow();

        List<UserLike> likedItems = userLikeRepository.findByUser(user);
        List<MediaDTO> likedMedia = new ArrayList<>();

        for (UserLike like : likedItems) {
            if (like.getMovie() != null) {
                likedMedia.add(new MediaDTO(
                        like.getMovie().getId(),
                        like.getMovie().getTitle(),
                        like.getMovie().getOverview(),
                        like.getMovie().getPosterPath(),
                        like.getMovie().getBackdropPath(),
                        "movie"
                ));
            }
            if (like.getTvSerie() != null) {
                likedMedia.add(new MediaDTO(
                        like.getTvSerie().getId(),
                        like.getTvSerie().getName(),
                        like.getTvSerie().getOverview(),
                        like.getTvSerie().getPosterPath(),
                        like.getTvSerie().getBackdropPath(),
                        "tv_series"
                ));
            }
        }
        return likedMedia;
    }

    public List<MediaDTO> getWatchlistMedia(int userId) {
        UserInfo user = userRepository.findById(userId).orElseThrow();

        List<UserWatchList> watchlistItems = userWatchlistRepository.findByUser(user);
        List<MediaDTO> watchlistMedia = new ArrayList<>();

        for (UserWatchList watch : watchlistItems) {
            if (watch.getMovie() != null) {
                watchlistMedia.add(new MediaDTO(
                        watch.getMovie().getId(),
                        watch.getMovie().getTitle(),
                        watch.getMovie().getOverview(),
                        watch.getMovie().getPosterPath(),
                        watch.getMovie().getBackdropPath(),
                        "movie"
                ));
            }
            if (watch.getTvSerie() != null) {
                watchlistMedia.add(new MediaDTO(
                        watch.getTvSerie().getId(),
                        watch.getTvSerie().getName(),
                        watch.getTvSerie().getOverview(),
                        watch.getTvSerie().getPosterPath(),
                        watch.getTvSerie().getBackdropPath(),
                        "tv_series"
                ));
            }
        }
        return watchlistMedia;
    }

	public boolean existsByEmail(String email) {
        UserInfo user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }
}