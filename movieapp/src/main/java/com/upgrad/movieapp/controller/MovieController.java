package com.upgrad.movieapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.movieapp.dto.MovieBookingDTO;
import com.upgrad.movieapp.dto.MovieDTO;
import com.upgrad.movieapp.entity.Theatre;
import com.upgrad.movieapp.entity.User;
import com.upgrad.movieapp.entity.Movie;
import com.upgrad.movieapp.service.MovieService;

@RestController
@RequestMapping(value="/movie_app/v1")
public class MovieController {
	
	@Autowired
	private MovieService movieService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value="/movies",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createMovie(MovieDTO movieDto) {
		//Convert movieDto to movie entity
		Movie newMovie = modelMapper.map(movieDto, Movie.class);
		Movie savedMovie = movieService.acceptMovieDetails(newMovie);
		
		MovieDTO savedMovieDto = modelMapper.map(savedMovie, MovieDTO.class);
		return new ResponseEntity(savedMovieDto,HttpStatus.CREATED);
		
	}
	
	@GetMapping(value ="/movies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllMovies() {
		List<Movie> movies = movieService.getAllMovies();
		
		List<MovieDTO> movieDtoList  = new ArrayList<>();
		for(Movie movie : movies) {
			movieDtoList.add(modelMapper.map(movie, MovieDTO.class));
		}
		
		return new ResponseEntity(movieDtoList,HttpStatus.OK);
	}
	
	@GetMapping(value ="/movies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getMovieBasedOnId(@PathVariable(name = "id") int id) {
		Movie responseMovie = movieService.getMovieDetails(id);
		MovieDTO responseMovieDto = modelMapper.map(responseMovie, MovieDTO.class);
		
		return new ResponseEntity(responseMovieDto,HttpStatus.OK);
		
	}
 
	@PutMapping(value ="/movies/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateMovieDetails(@PathVariable(name = "id") int id, @RequestBody MovieDTO movieDto) {
		Movie requestMovie  = modelMapper.map(movieDto, Movie.class);
		Movie updatedMovie  = movieService.updateMovieDetails(id, requestMovie);
		
		MovieDTO responseMovieDto = modelMapper.map(updatedMovie, MovieDTO.class);
		
		
		return new ResponseEntity(responseMovieDto,HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/bookings/movie" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity bookMovieDetails(@RequestBody MovieBookingDTO movieBookingDTO){
	    Movie requestedMovie = modelMapper.map(movieBookingDTO.getMovie(),Movie.class);
	    User fromUser = modelMapper.map(movieBookingDTO.getUser(),User.class);
	    Theatre requestedTheatre = modelMapper.map(movieBookingDTO.getTheatre(), Theatre.class);

	    boolean isValidBooking = movieService.bookMovie(fromUser,requestedMovie,requestedTheatre);

	    if(!isValidBooking)
	      return new ResponseEntity("Not Booked !!", HttpStatus.OK) ;
	    return new ResponseEntity("Booked Successfully !!", HttpStatus.OK) ;
	  }
}
