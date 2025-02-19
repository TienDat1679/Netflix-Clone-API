package com.backend.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String overview;
    private Date firstAirDate;
    private String posterPath;
    private String backdropPath;
    private boolean adult;

    @OneToMany(mappedBy = "tvSerie")
    private List<Episode> episodes;

    @ManyToMany
    @JoinTable(name = "tv_genres", joinColumns = @JoinColumn(name = "series_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
}
