package com.imbd.imbd.entity;

import com.imbd.imbd.enums.Genre;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @NonNull
    private String movieName;
    @NonNull
    private String description;
    @NonNull
    private String language;
    @NonNull
    private String studio;
    @NonNull
    private Integer duration;
    @NonNull
    private String producer;
    @NonNull
    private String director;
    @NonNull
    private String releaseYear;
    @NonNull
    private Genre genre;
    @NonNull
    private Float avgRating;
    @NonNull
    private LocalDateTime timestamp;
}
