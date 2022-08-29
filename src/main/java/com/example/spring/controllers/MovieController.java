package com.example.spring.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.spring.entities.Movie;
import com.example.spring.service.MovieService;

@Controller
@RequestMapping(path="/movies/")
public class MovieController {
	
	private MovieService movieService;
	
	@Autowired
	public void setMovieService(MovieService movieService) {
		this.movieService = movieService;
	}
	
	/*
	 * JSON methods require @ResponseBody
	 */
	@GetMapping(path="{id}")
	@ResponseBody
	public Optional<Movie> getMovie(@PathVariable(name="id") Long id) {
		return movieService.getMovie(id);
	}
	
	@GetMapping(path="/list")
	@ResponseBody
	public List<Movie> getMovies() {
		return movieService.findAll();
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Movie saveMovie(@RequestBody Movie movieToSave) {
		return movieService.saveMovie(movieToSave);
	}
	
	@PutMapping(path="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Movie updateMovie(@RequestBody Movie movieToUpdate, @PathVariable(name="id") Long id) {
		return movieService.updateMovie(movieToUpdate, id);
	}
	
	@DeleteMapping(path="{id}")
	@ResponseBody
	public void deleteMovie(@PathVariable(name="id") Long id) {
		movieService.deleteMovie(id);
	}

	/*
	 * REST methods do not require @ResponseBody
	 * Return a String redirect to html page in the template folder
	 */
	@GetMapping(path="/")
	public String index() {
		return "index";
	}
	
	@GetMapping(path="/add")
	public String addMovie(Model model){
		model.addAttribute("movie", new Movie());
		return "editMovie";
	}
	
	@PostMapping(path="/store")
	public String storeMovie(Movie movie) {
		movieService.saveMovie(movie);
		return "redirect:/";
	}
	
	@GetMapping(path="/findAll")
	public String getAllMovies(Model model) {
		model.addAttribute("movies", movieService.findAll());
		return "listMovies";
	}
	
	@GetMapping(path="/update/{id}")
	public String updateMovie(Model model, @PathVariable(value="id") Long id) {
		model.addAttribute("movie", movieService.getMovie(id));
		return "editMovie";
	}
	
	@GetMapping(path="/delete/{id}")
	public String removeMovie(@PathVariable(value="id") Long id) {
		movieService.deleteMovie(id);
		return "redirect:/movies/findAll";
	}
}
