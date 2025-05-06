package com.backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private boolean adult;
    private boolean video;
    private int runtime;
    private Long voteCount;
    private Long viewCount;

//    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Trailer> trailers;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "movie_credits", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "credit_id"))
    private List<Credit> credits;

}
