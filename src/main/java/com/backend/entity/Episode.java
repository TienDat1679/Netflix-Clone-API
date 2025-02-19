package com.backend.entity;

import java.util.Date;

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
@Table(name = "episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int episodeNumber;
    private String name;
    private String overview;
    private int duration;
    private String videoUrl;
    private Date airDate;
    private int seasonNumber;
    private int runtime;
    private String stillPath;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private TVSerie tvSerie;
}
