package com.example.spring.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.entities.Movie;
import com.example.spring.repository.MovieRepository;

@Service
public class MovieService {
	
	private MovieRepository movieRepository;
	
	@Autowired
	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	private Logger log = LoggerFactory.getLogger(MovieService.class);

	public Optional<Movie> getMovie(Long id) {
		return movieRepository.findById(id);
	}
	
	public Movie saveMovie(Movie movieToSave) {
		return movieRepository.save(movieToSave);
	}
	
	public Movie updateMovie(Movie movieToUpdate, Long id) {
		//find the object in the repository
		Optional<Movie> foundMovie = movieRepository.findById(id);
		if (foundMovie.isPresent()) {
			Movie found = foundMovie.get(); 
			found.setImagePath(movieToUpdate.getImagePath());
			found.setTitle(movieToUpdate.getTitle());
			found.setReleaseDate(movieToUpdate.getReleaseDate());
			found.setVoteAverage(movieToUpdate.getVoteAverage());
			return movieRepository.save(found);
		}
		else {
			log.info("No movie found with given id");
			return movieToUpdate;
		}
	}
	
	public void deleteMovie(Long id) {
		movieRepository.deleteById(id);
	}
	
	public List<Movie> findAll(){
		return movieRepository.findAll();
	}
}
