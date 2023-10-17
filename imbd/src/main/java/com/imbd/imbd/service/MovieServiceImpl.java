package com.imbd.imbd.service;

import com.imbd.imbd.dto.MovieDetailsDTO;
import com.imbd.imbd.dto.PaginatedResponseDTO;
import com.imbd.imbd.dto.RatingDTO;
import com.imbd.imbd.dto.ResponseDTO;
import com.imbd.imbd.entity.Movie;
import com.imbd.imbd.entity.Rating;
import com.imbd.imbd.entity.User;
import com.imbd.imbd.enums.Genre;
import com.imbd.imbd.exception.customExceptions.InvalidRatingException;
import com.imbd.imbd.repository.MovieRepository;
import com.imbd.imbd.repository.RatingRepository;
import com.imbd.imbd.repository.UserRepository;
import com.imbd.imbd.service.interfaces.IDTOBuilderService;
import com.imbd.imbd.service.interfaces.IMovieService;
import com.imbd.imbd.service.interfaces.IRecommendationService;
import com.imbd.imbd.specification.MovieSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieServiceImpl implements IMovieService {

    private final static Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final IDTOBuilderService dtoBuilderService;
    private final IRecommendationService recommendationService;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository, IRecommendationService recommendationService,IDTOBuilderService dtoBuilderService){

        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.recommendationService = recommendationService;
        this.dtoBuilderService = dtoBuilderService;
    }

    @Override
    public ResponseEntity<?> addMovie(MovieDetailsDTO movie) {
        Genre genre = validateGenre(movie.getGenre());
        Movie newMovie = createMovieFromDTO(movie,genre);
        Movie savedMovie = movieRepository.save(newMovie);
        logger.info("Movie added into database at {}", LocalDateTime.now());
        ResponseDTO<Movie> responseDTO = dtoBuilderService.createResponse(savedMovie,HttpStatus.CREATED,"Movie Successfully added");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<?> getAllMovies(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
       Page<Movie> moviePage = movieRepository.findAll(pageable);
        logger.info("Got all Movies from database {}", LocalDateTime.now());
        PaginatedResponseDTO<Movie> responseDTO = dtoBuilderService.createPaginatedData(moviePage,HttpStatus.OK,"Successfully retrived All movies");
        return  ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<?> getMovieByName(String movieName) {
        Movie movie = movieRepository.findFirstBymovieName(movieName);
        logger.info("Getting movie by movie name '{}' at {}",movieName,LocalDateTime.now());
        ResponseDTO<Movie> responseDTO = dtoBuilderService.createResponse(movie,HttpStatus.OK,"Successfully retrived Movie with name");
        return  ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<?> getFilteredMovies(String studio,String language,String director,String releaseYear, String genre,Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Specification<Movie> specification = MovieSpecification.withCriteria(studio, language, director,releaseYear,genre);
        logger.info(" Getting filterd Movies with specification '{}' at {}",specification, LocalDateTime.now());
        Page<Movie> filteredMovies = movieRepository.findAll(specification,pageable);
        PaginatedResponseDTO<Movie> responseDTO = dtoBuilderService.createPaginatedData(filteredMovies,HttpStatus.OK,"Successfully Filtered Movies");
        return  ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<?> addRating(Long movieId, RatingDTO rating, Long userId) {
        logger.info("Attempting to add Rating to movieId '{}' at {}",movieId, LocalDateTime.now());
        Float validatedRating = validateRating(rating.getRating());
        Movie validatedMovie = validateMovie(movieId);
        User validatedUser = validateUser(userId);
        Rating newRating = createRating(validatedMovie,validatedRating,validatedUser);
        ratingRepository.save(newRating);
        validatedMovie.setAvgRating(updateAverageRating(movieId,newRating,validatedMovie.getAvgRating()));
        movieRepository.save(validatedMovie);
        ResponseDTO<Rating> responseDTO = dtoBuilderService.createResponse(newRating,HttpStatus.CREATED,"Successfully added rating ");
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<?> getRecommendation(Long userId) {
        List movieList = recommendationService.getRecommendedMovies(userId);
        logger.info("Attempting to recommend movies to user '{}' at {}",userId, LocalDateTime.now());
        ResponseDTO<List<Movie>> responseDTO = dtoBuilderService.createResponse(movieList,HttpStatus.OK,"Recommended movies");
        return  ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    private Genre validateGenre(String genreStr) {
        try {
            return Genre.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid genre '{}' entered at {}.", genreStr, LocalDateTime.now());
            throw new IllegalArgumentException("Invalid genre: " + genreStr);
        }
    }

    private User validateUser(Long userId){
        try{
            return userRepository.findById(userId).get();
        }catch (IllegalArgumentException e) {
            logger.error("Invalid userId '{}' entered at {}.", userId, LocalDateTime.now());
            throw new IllegalArgumentException("Invalid genre: " + userId);
        }
    }

    private Movie validateMovie(Long movieId){
        try {
            return movieRepository.findById(movieId).get();
        }catch (IllegalArgumentException e) {
            logger.error("Invalid userId '{}' entered at {}.", movieId, LocalDateTime.now());
            throw new IllegalArgumentException("Invalid genre: " + movieId);
        }
    }

    private Float validateRating(Float rating){
        if(rating>5 || rating <0)
            throw new InvalidRatingException("Invalid rating entered, Please add number between 1 to 5");
        return rating;
    }

    private Movie createMovieFromDTO(MovieDetailsDTO movie, Genre genre) {
        return Movie.builder()
                .movieName(movie.getMovieName())
                .description(movie.getDescription())
                .language(movie.getLanguage())
                .studio(movie.getStudio())
                .duration(movie.getDuration())
                .producer(movie.getProducer())
                .director(movie.getDirector())
                .releaseYear(movie.getReleaseYear())
                .avgRating(0F)
                .genre(genre)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Rating createRating(Movie movie, Float rating, User user){
        return Rating.builder()
                .rating(rating)
                .movie(movie)
                .user(user)
                .build();
    }

private Float updateAverageRating(Long movieId, Rating rating, Float currentAvgRating) {
    Long numberOfRatings = ratingRepository.countByMovie_movieId(movieId);

    if (numberOfRatings == 0) {
        return 0.0F;
    } else if (numberOfRatings == 1) {
        return rating.getRating();
    } else {
        float newAvgRating = (currentAvgRating * (numberOfRatings - 1) + rating.getRating()) / numberOfRatings;
        return Math.round(newAvgRating * 100.0) / 100.0F;
    }
}
}
