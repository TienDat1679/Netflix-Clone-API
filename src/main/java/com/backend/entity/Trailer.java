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
@Table(name = "trailers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trailer {

    @Id
    private String id;

    @Column(name = "video_key")
    private String key;
    private String name;
    private String site;
    private String type;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private TVSerie tvSerie;
}
