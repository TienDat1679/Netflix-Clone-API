package com.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.MailBody;
import com.backend.dto.MediaDTO;
import com.backend.dto.request.AddToWatchListRequest;
import com.backend.dto.request.LikeRequest;
import com.backend.dto.request.UserCreationRequest;
import com.backend.dto.request.UserUpdateRequest;
import com.backend.dto.response.UserResponse;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.entity.UserInfo;
import com.backend.entity.UserLike;
import com.backend.entity.UserWatchList;
import com.backend.enums.Role;
import com.backend.exception.AppException;
import com.backend.exception.ErrorCode;
import com.backend.mapper.MediaMapper;
import com.backend.mapper.UserMapper;
import com.backend.repository.MovieRepository;
import com.backend.repository.RoleRepository;
import com.backend.repository.TVSerieRepository;
import com.backend.repository.UserInfoRepository;
import com.backend.repository.UserLikeRepository;
import com.backend.repository.UserWatchListRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	UserInfoRepository userRepository;
    MovieRepository movieRepository;
    TVSerieRepository tvSerieRepository;
    UserLikeRepository userLikeRepository;
    UserWatchListRepository userWatchlistRepository;
    RoleRepository roleRepository;
    MediaMapper mediaMapper;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailService emailService;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);
        
        UserInfo user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findById(Role.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        var roles = List.of(role);
        user.setRoles(new HashSet<>(roles));

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

    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUser(String userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        UserInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse verifyUser(String email, Integer otp) {
        UserInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        user.setEnabled(1);
        user.setOtp(null); // Clear OTP after successful verification
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String resendOtp(String email) {
        UserInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Integer otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for verify your Account request: " + otp)
                .subject("OTP for Verify Account request")
                .build();
        emailService.sendSimpleMessage(mailBody);

        user.setOtp(otp);
        userRepository.save(user);

        return otp.toString();
    }

    @PreAuthorize("hasRole('USER')")
    public void likeMedia(String userId, LikeRequest request) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserLike userLike = new UserLike();
        userLike.setUser(user);

        if ("movie".equalsIgnoreCase(request.getType())) {
            Movie movie = movieRepository.findById(request.getMediaId())
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            userLike.setMovie(movie);
            movieRepository.incrementVoteCount(request.getMediaId());
        } else if ("tv_series".equalsIgnoreCase(request.getType())) {
            TVSerie tvSeries = tvSerieRepository.findById(request.getMediaId())
                    .orElseThrow(() -> new AppException(ErrorCode.TV_SERIES_NOT_FOUND));
            userLike.setTvSerie(tvSeries);
            tvSerieRepository.incrementVoteCount(request.getMediaId());
        } else {
            throw new AppException(ErrorCode.INVALID_MEDIA_TYPE);
        }

        userLikeRepository.save(userLike);
    }

    @PreAuthorize("hasRole('USER')")
    public void addToWatchlist(String userId, AddToWatchListRequest request) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserWatchList watchlist = new UserWatchList();
        watchlist.setUser(user);

        if ("movie".equalsIgnoreCase(request.getType())) {
            Movie movie = movieRepository.findById(request.getMediaId())
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            watchlist.setMovie(movie);
        } else if ("tv_series".equalsIgnoreCase(request.getType())) {
            TVSerie tvSeries = tvSerieRepository.findById(request.getMediaId())
                    .orElseThrow(() -> new AppException(ErrorCode.TV_SERIES_NOT_FOUND));
            watchlist.setTvSerie(tvSeries);
        } else {
            throw new AppException(ErrorCode.INVALID_MEDIA_TYPE);
        }

        userWatchlistRepository.save(watchlist);
    }

    @PreAuthorize("hasRole('USER')")
    public List<MediaDTO> getLikedMedia(String userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserLike> likedItems = userLikeRepository.findByUser(user);
        return likedItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public List<MediaDTO> getWatchlistMedia(String userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserWatchList> watchlistItems = userWatchlistRepository.findByUser(user);
        return watchlistItems.stream()
                .map(mediaMapper::toMediaDTO)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public boolean isMediaLiked(String userId, Long mediaId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(mediaId).orElse(null);
        TVSerie serie = tvSerieRepository.findById(mediaId).orElse(null);
        if (movie != null) {
            return userLikeRepository.existsByUserAndMovie(user, movie);
        } 
        return userLikeRepository.existsByUserAndTvSerie(user, serie);
    }

    @PreAuthorize("hasRole('USER')")
    public boolean isMediaInWatchlist(String userId, Long mediaId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(mediaId).orElse(null);
        TVSerie serie = tvSerieRepository.findById(mediaId).orElse(null);
        if (movie != null) {
            return userWatchlistRepository.existsByUserAndMovie(user, movie);
        }
        return userWatchlistRepository.existsByUserAndTvSerie(user, serie);
    }

    @PreAuthorize("hasRole('USER')")
    public void removeFromWatchlist(String userId, Long mediaId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(mediaId).orElse(null);
        TVSerie serie = tvSerieRepository.findById(mediaId).orElse(null);
        if (movie != null) {
            userWatchlistRepository.deleteByUserAndMovie(user, movie);
        } else {
            userWatchlistRepository.deleteByUserAndTvSerie(user, serie);
        }
    }

    @PreAuthorize("hasRole('USER')")
    public void unlikeMedia(String userId, Long mediaId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Movie movie = movieRepository.findById(mediaId).orElse(null);
        TVSerie serie = tvSerieRepository.findById(mediaId).orElse(null);
        if (movie != null) {
            userLikeRepository.deleteByUserAndMovie(user, movie);
            movieRepository.decrementVoteCount(mediaId);
        } else {
            userLikeRepository.deleteByUserAndTvSerie(user, serie);
            tvSerieRepository.decrementVoteCount(mediaId);
        }
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}