package com.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "tv_series")
public class TVSerie {

    @Id
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String firstAirDate;
    private String posterPath;
    private String backdropPath;
    private boolean adult;
    private Long voteCount;
    private Long viewCount;

//    @JsonIgnore
    @OneToMany(mappedBy = "tvSerie")
    private List<Episode> episodes;

//    @JsonIgnore
    @OneToMany(mappedBy = "tvSerie")
    private List<Trailer> trailers;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "tv_genres", joinColumns = @JoinColumn(name = "series_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "serie_credits", joinColumns = @JoinColumn(name = "series_id"), inverseJoinColumns = @JoinColumn(name = "credit_id"))
    private List<Credit> credits;
}
