package com.imbd.imbd.service.interfaces;

import com.imbd.imbd.entity.Movie;

import java.util.List;

public interface IRecommendationService {
    List<Movie> getRecommendedMovies(Long userId);
}
