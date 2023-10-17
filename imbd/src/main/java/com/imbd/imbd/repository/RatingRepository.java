package com.imbd.imbd.repository;

import com.imbd.imbd.entity.Movie;
import com.imbd.imbd.entity.Rating;
import com.imbd.imbd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByUser(User user);
    List<Rating> findByMovie(Movie movie);
    Long countByMovie_movieId(Long movieId);
}
