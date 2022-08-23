package com.example.spring.boot;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.spring.entities.Movie;
import com.example.spring.repository.MovieRepository;

@Component
public class DataLoader implements CommandLineRunner{

	private Logger log = LoggerFactory.getLogger(DataLoader.class);
	
	private MovieRepository movieRepository;

	@Autowired
	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}
	
	private String getImagePath() {
		Random random = new Random();
		int imagePathId = random.nextInt(100);
		StringBuilder path = new StringBuilder("https://picsum.photos/seed/"+imagePathId+"/300/200");
		return path.toString();
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		if (movieRepository.count()<1) {
			Movie movie1 = new Movie();
			movie1.setImagePath(getImagePath());
			movie1.setTitle("Teste");
			movie1.setReleaseDate("10/10/10");
			movie1.setVoteAverage(4.5);

			movieRepository.save(movie1);

			Movie movie2 = new Movie();
			movie2.setImagePath(getImagePath());
			movie2.setTitle("Teste2");
			movie2.setReleaseDate("11/11/11");
			movie2.setVoteAverage(3.7);

			movieRepository.save(movie2);

			Movie movie3 = new Movie();
			movie3.setImagePath(getImagePath());
			movie3.setTitle("Teste3");
			movie3.setReleaseDate("22/22/22");
			movie3.setVoteAverage(2.5);

			movieRepository.save(movie3);

//			List<Movie> movies = movieRepository.findAll();
//
//			for (Movie movie: movies) {
//				log.info("Movie found: " + movie.toString());
//			}
//
//			List<Movie> moviesByTitle = movieRepository.findByTitle("Teste");
//
//			for (Movie movie: moviesByTitle) {
//				log.info("Movie found: " + movie.toString());
//			}
		}
		log.info("Movie count in database: " + movieRepository.count());

	}
}
