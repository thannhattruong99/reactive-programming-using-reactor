package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {
  private MovieInfoService movieInfoService = new MovieInfoService();
  private ReviewService reviewService = new ReviewService();
  MovieReactiveService movieReactiveService =
      new MovieReactiveService(movieInfoService, reviewService);

  @Test
  void getAllMovie() {
    var moviesFlux = movieReactiveService.getAllMovie();
    StepVerifier.create(moviesFlux)
        .assertNext(
            movie -> {
              assertEquals("Batman Begins", movie.getMovie().getName());
              assertEquals(2, movie.getReviewList().size());
            })
        .assertNext(
            movie -> {
              assertEquals("The Dark Knight", movie.getMovie().getName());
              assertEquals(2, movie.getReviewList().size());
            })
        .assertNext(
            movie -> {
              assertEquals("Dark Knight Rises", movie.getMovie().getName());
              assertEquals(2, movie.getReviewList().size());
            })
        .verifyComplete();
  }

  @Test
  void getMovieById() {
      long movieId = 100L;
      var movieMono = movieReactiveService.getMovieById(movieId).log();
      StepVerifier.create(movieMono).assertNext(movie -> {
          assertEquals("Batman Begins", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
      }).verifyComplete();
  }
}
