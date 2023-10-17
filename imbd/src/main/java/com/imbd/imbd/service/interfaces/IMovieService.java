package com.imbd.imbd.service.interfaces;

import com.imbd.imbd.dto.MovieDetailsDTO;
import com.imbd.imbd.dto.RatingDTO;
import org.springframework.http.ResponseEntity;

public interface IMovieService {
    ResponseEntity<?> addMovie(MovieDetailsDTO movie);

    ResponseEntity<?> getAllMovies(Integer page, Integer size);

    ResponseEntity<?> getMovieByName(String movieName);

    ResponseEntity<?> getFilteredMovies(String studio,String language, String director, String releaseYear, String genre,Integer page, Integer size );

    ResponseEntity<?> addRating(Long movieId, RatingDTO rating, Long userId);

    ResponseEntity<?> getRecommendation(Long userId);
}
