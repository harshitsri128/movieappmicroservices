package com.upgrad.movieapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.upgrad.movieapp.dao.MovieDao;
import com.upgrad.movieapp.entity.Theatre;
import com.upgrad.movieapp.entity.User;
import com.upgrad.movieapp.entity.Movie;

@Service
public class MovieServiceImpl implements MovieService{
	
	@Autowired
	private MovieDao movieDao;
	
	@Autowired
	 private RestTemplate restTemplate;
	
	@Value("${userApp.url}")
	private String userAppUrl;
	
	@Value("${theatreApp.url}")
	private String theatreAppUrl;
	
	@Override
	public Movie acceptMovieDetails(Movie movie) {
		
		return movieDao.save(movie);
	}

	@Override
	public List<Movie> acceptMultipleMovieDetails(List<Movie> movies) {
		List<Movie> savedMovies  = new ArrayList<>();
		for(Movie movie: movies) {
			savedMovies.add(acceptMovieDetails(movie));
		}
		return savedMovies;
	}

	@Override
	public Movie getMovieDetails(int id) {
		
		return movieDao.findById(id).get();
	}

	@Override
	public Movie updateMovieDetails(int id, Movie movie) {
		Movie savedMovie = getMovieDetails(id);
		savedMovie.setDuration(movie.getDuration());
		savedMovie.setCoverPhotoUrl(movie.getCoverPhotoUrl());
		savedMovie.setTrailerUrl(movie.getTrailerUrl());
		savedMovie.setReleaseDate(movie.getReleaseDate());
		savedMovie.setMovieName(movie.getMovieName());
		savedMovie.setMovieDescription(movie.getMovieDescription());
		return movieDao.save(savedMovie);
	}

	@Override
	public boolean deleteMovie(int id) {
		Movie savedMovie = getMovieDetails(id);
		if(savedMovie == null)
		return false;
		else {
			movieDao.delete(savedMovie);
			return true;
		}
	}

	@Override
	public List<Movie> getAllMovies() {
		
		return movieDao.findAll();
	}

	@Override
	public Page<Movie> getMovieDetailsPaginated(Pageable pageable) {
		
		return movieDao.findAll(pageable);
	}

	@Override
	  public Boolean bookMovie(User user, Movie movie, Theatre theatre) {

	    //Check whether requested movie is a valid movie.
	    Optional<Movie> requestedMovie = movieDao.findById(movie.getMovieId());
	    if(!requestedMovie.isPresent())
	      return false;

	    //Check whether User is valid
	    Map<String, String> userUriMap = new HashMap<>();
	    userUriMap.put("id",String.valueOf(user.getUserId()));
	    //String userAppUrl = "http://localhost:8080/user_app/v1/users/{id}";
	    User receivedUser = (restTemplate.getForObject(userAppUrl,User.class,userUriMap));
	    if(receivedUser==null){
	      return false;
	    }

	    //Check whether theatre and movie combination is valid
	    Map<String, String> theatreUriMap = new HashMap<>();
	    theatreUriMap.put("theatreId",String.valueOf(theatre.getTheatreId()));
	    theatreUriMap.put("movieId",String.valueOf(theatre.getMovieId()));
	    //String theatreAppUrl = "http://localhost:8082/theatre_app/v1/theatres/{theatreId}/movie/{movieId}";
	    Theatre receivedTheatre = (restTemplate.getForObject(theatreAppUrl,Theatre.class,theatreUriMap));
	    if(receivedTheatre==null){
	      return false;
	    }

	    return true;
	  }
}
