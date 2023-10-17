package com.imbd.imbd.controller;

import com.imbd.imbd.dto.MovieDetailsDTO;
import com.imbd.imbd.dto.RatingDTO;
import com.imbd.imbd.service.interfaces.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imdb")
public class MovieController {
    private  final IMovieService movieService;

    @Autowired
    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody MovieDetailsDTO movieDetailsDTO){
        return movieService.addMovie(movieDetailsDTO);
    }

    @PostMapping("/movies/ratings")
    public ResponseEntity<?> addRating(@RequestParam Long movieId,@RequestParam Long userId,@RequestBody RatingDTO ratingDTO){
        return movieService.addRating(movieId,ratingDTO,userId);
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getAllMovies(@RequestParam(defaultValue = "0")int page,
                                          @RequestParam(defaultValue = "5")int size){
        return movieService.getAllMovies(page,size);
    }

    @GetMapping("/movies/movieName")
    public ResponseEntity<?> getMoviebyMovieName(@RequestParam String movieName){
        return movieService.getMovieByName(movieName);
    }

    @GetMapping("/movies/filter")
    public ResponseEntity<?> getFilteredMovies(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5")int size,
            @RequestParam(required = false)String studio,
            @RequestParam(required = false)String language,
            @RequestParam(required = false)String director,
            @RequestParam(required = false)String genre,
            @RequestParam(required = false)String releaseYear
    ){
        return movieService.getFilteredMovies(studio,language,director,releaseYear,genre,page,size);
    }

    @GetMapping("/movies/forYou")
    public ResponseEntity<?>getRecommendedMovies(@RequestParam Long userId){
        return movieService.getRecommendation(userId);
    }

}
