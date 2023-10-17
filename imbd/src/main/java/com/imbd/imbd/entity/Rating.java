package com.imbd.imbd.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @ManyToOne
    private User user;
    @ManyToOne
    private Movie movie;
    @NonNull
    private Float rating;
}
