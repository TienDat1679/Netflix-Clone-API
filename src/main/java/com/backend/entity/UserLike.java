package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLike {
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
