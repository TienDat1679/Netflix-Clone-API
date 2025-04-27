package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PlaybackProgress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaybackProgress {

    private Long position;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ppid;

    private String userId;

    private Long mediaId;

}
