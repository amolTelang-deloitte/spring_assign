package com.imbd.imbd.service;

import com.imbd.imbd.entity.Movie;
import com.imbd.imbd.entity.Rating;
import com.imbd.imbd.entity.User;
import com.imbd.imbd.repository.MovieRepository;
import com.imbd.imbd.repository.RatingRepository;
import com.imbd.imbd.repository.UserRepository;
import com.imbd.imbd.service.interfaces.IRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements IRecommendationService {
    private final static Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public RecommendationServiceImpl(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Movie> getRecommendedMovies(Long userId) {
        logger.info("generating recommendation for user'{}' at {}",userId, LocalDateTime.now());
        User user = userRepository.findById(userId).get();
        Map<String, Double> userPreferences = calculateUserPreferences(user);
        List<Rating> userRatings = ratingRepository.findByUser(user);
        Set<Long> watchedMovieIds = userRatings.stream()
                .map(rating -> rating.getMovie().getMovieId())
                .collect(Collectors.toSet());
        List<Movie> recommendedMovies = movieRepository.findAll()
                .stream()
                .filter(movie ->
                        !watchedMovieIds.contains(movie.getMovieId()) &&
                                (userPreferences.containsKey(movie.getGenre().toString()) || userPreferences.containsKey(movie.getDirector()))
                )
                .sorted((movie1, movie2) -> {
                    double score1 = calculateMovieRelevance(userPreferences, movie1);
                    double score2 = calculateMovieRelevance(userPreferences, movie2);
                    return Double.compare(score2, score1);
                })
                .collect(Collectors.toList());
        logger.info("finished generating movie recommendation for user '{}' at {}",userId, LocalDateTime.now());
        return recommendedMovies;
    }

    private Map<String, Double> calculateUserPreferences(User user) {
        logger.info("Calculating user preference for user'{}' at {}",user.getUserId(), LocalDateTime.now());
        Map<String, Double> preferences = new HashMap<>();
        List<Rating> userRatings = ratingRepository.findByUser(user);

        userRatings
                .forEach(rating -> {
                    Movie movie = rating.getMovie();
                    String genre = movie.getGenre().toString();
                    String director = movie.getDirector();

                    if (!preferences.containsKey(genre)) {
                        preferences.put(genre, Double.valueOf(rating.getRating()));
                    } else {
                        preferences.put(genre, (preferences.get(genre) + rating.getRating())/2);
                    }

                    if (!preferences.containsKey(director)) {
                        preferences.put(director, Double.valueOf(rating.getRating()));
                    } else {
                        preferences.put(director, (preferences.get(director) + rating.getRating())/2);
                    }
                });
        return preferences;
    }

    private double calculateMovieRelevance(Map<String, Double> userPreferences, Movie movie) {
        logger.info("Calculating relevance for movie '{}' at {}",movie, LocalDateTime.now());
        double relevance = 0.0;
        String genre = movie.getGenre().toString();
        String director = movie.getDirector();
        if (userPreferences.containsKey(genre)) {
            relevance += userPreferences.get(genre);
        }
        if (userPreferences.containsKey(director)) {
            relevance += userPreferences.get(director);
        }
        return relevance;
    }


}
