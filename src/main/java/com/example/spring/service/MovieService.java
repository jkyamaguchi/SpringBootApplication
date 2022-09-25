package com.example.spring.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.entities.Movie;
import com.example.spring.entities.MovieDetails;
import com.example.spring.repository.MovieDetailsRepository;
import com.example.spring.repository.MovieRepository;

@Service
public class MovieService {
	
	private MovieRepository movieRepository;
	private MovieDetailsRepository movieDetailsRepository;
	
	@Autowired
	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}
	
	@Autowired
	public void setMovieDetailsRepository(MovieDetailsRepository movieDetailsRepository) {
		this.movieDetailsRepository = movieDetailsRepository;
	}

	private Logger log = LoggerFactory.getLogger(MovieService.class);

	/*
	 * This method returns a copy of movie without details (MovieDetails)
	 * A copy is necessary to return a object to JSON service (just Movie, without MovieDetails)
	 * If the object is null, a message error of NullPointer is send through Advice class.
	 * 
	 */
	public Movie getMovie(Long id) {
		Optional<Movie> found = movieRepository.findById(id);
		 Movie movie = found.get();
         return Movie.builder()
         		.id(movie.getId())
         		.imagePath(movie.getImagePath())
         		.title(movie.getTitle())
         		.releaseDate(movie.getReleaseDate())
         		.voteAverage(movie.getVoteAverage())
         		.build();
	}
	
	public Optional<Movie> getMovieDetails(Long id) {
		return movieRepository.findById(id);
	}
	
	public Movie saveMovie(Movie movieToSave) {
		return movieRepository.save(movieToSave);
	}
	
	public Movie updateMovie(Movie movieToUpdate, Long id) {
		//find the object in the repository
		Optional<Movie> foundMovie = movieRepository.findById(id);
		
		if (foundMovie.isPresent()) {
			Movie update = foundMovie.get();
			update.setImagePath(movieToUpdate.getImagePath());
			update.setTitle(movieToUpdate.getTitle());
			update.setReleaseDate(movieToUpdate.getReleaseDate());
			update.setVoteAverage(movieToUpdate.getVoteAverage());

			Optional<MovieDetails> foundDetails = movieDetailsRepository.findById(foundMovie.get().getDetails().getId());
			
			MovieDetails updateDetails = foundDetails.get();
			updateDetails.setOverview(movieToUpdate.getDetails().getOverview());
			updateDetails.setGenres(movieToUpdate.getDetails().getGenres());
			
			update.getDetails().setOverview(movieToUpdate.getDetails().getOverview());
			update.getDetails().setGenres(movieToUpdate.getDetails().getGenres());
			
			//movieDetailsRepository.save(updateDetails);
			return movieRepository.save(update);
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
