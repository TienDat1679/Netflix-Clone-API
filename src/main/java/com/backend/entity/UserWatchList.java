package com.backend.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "user_watchlist")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne(optional = true) // Có thể NULL nếu là TVSerie
    @JoinColumn(name = "movie_id", nullable = true)
    private Movie movie;

    @ManyToOne(optional = true) // Có thể NULL nếu là Movie
    @JoinColumn(name = "tv_series_id", nullable = true)
    private TVSerie tvSerie;
}