package com.upgrad.movieapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.upgrad.movieapp.entity.Theatre;
import com.upgrad.movieapp.entity.User;
import com.upgrad.movieapp.entity.Movie;

public interface MovieService {
	
	public Movie acceptMovieDetails(Movie movie);
	
	public List<Movie> acceptMultipleMovieDetails(List<Movie> movies);
	
	public Movie getMovieDetails(int id);
	
	public Movie updateMovieDetails(int id,Movie movie);
	
	public boolean deleteMovie(int id);
	
	public List<Movie> getAllMovies();
	
	public Page<Movie> getMovieDetailsPaginated(Pageable pageable);
	
	public Boolean bookMovie(User user, Movie movie, Theatre theatre);

}
