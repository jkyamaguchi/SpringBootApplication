package com.example.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
	
	//There can be more than one movie with the same name
	List<Movie> findByTitle(String title);
	
	Optional<Movie> findById(Long id);
}
