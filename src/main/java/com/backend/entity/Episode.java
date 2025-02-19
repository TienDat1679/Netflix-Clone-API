package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @Id
    private Long id;

    private int episodeNumber;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String airDate;
    private int seasonNumber;
    private int runtime;
    private String stillPath;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private TVSerie tvSerie;
}
