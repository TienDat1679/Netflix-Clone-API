package com.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    private Long id;

    private String name;
    private String knownForDepartment;
    private String profilePath;

    @JsonIgnore
    @ManyToMany(mappedBy = "credits")
    private List<Movie> movies;

    @ManyToMany(mappedBy = "credits")
    @JsonIgnore
    private List<TVSerie> series;
}
