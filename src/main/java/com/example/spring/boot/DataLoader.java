package com.example.spring.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.spring.entities.Genre;
import com.example.spring.entities.Movie;
import com.example.spring.entities.MovieDetails;
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
			
			List<Genre> list1 = new ArrayList<>();
			list1.add(Genre.ACTION);
			list1.add(Genre.MYSTERY);
			
			MovieDetails details1 = new MovieDetails();
			details1.setGenres(list1);
			
			Movie movie1 = new Movie();
			movie1.setImagePath(getImagePath());
			movie1.setTitle("Teste");
			movie1.setReleaseDate("10/10/10");
			movie1.setVoteAverage(4.5);
			movie1.setDetails(details1);
			movieRepository.save(movie1);

			List<Genre> list2 = new ArrayList<>();
			list2.add(Genre.COMEDY);
			list2.add(Genre.ROMANCE);
			list2.add(Genre.FANTASY);
			
			MovieDetails details2 = new MovieDetails();
			details2.setGenres(list2);

			Movie movie2 = new Movie();
			movie2.setImagePath(getImagePath());
			movie2.setTitle("Teste2");
			movie2.setReleaseDate("11/11/11");
			movie2.setVoteAverage(3.7);
			movie2.setDetails(details2);
			movieRepository.save(movie2);

			List<Genre> list3 = new ArrayList<>();
			list3.add(Genre.DRAMA);
			
			MovieDetails details3 = new MovieDetails();
			details3.setGenres(list3);

			Movie movie3 = new Movie();
			movie3.setImagePath(getImagePath());
			movie3.setTitle("Teste3");
			movie3.setReleaseDate("22/22/22");
			movie3.setVoteAverage(2.5);
			movie3.setDetails(details3);
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
