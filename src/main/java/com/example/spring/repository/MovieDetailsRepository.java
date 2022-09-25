package com.example.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.entities.MovieDetails;

@Repository
public interface MovieDetailsRepository extends JpaRepository<MovieDetails, Long> {

	Optional<MovieDetails> findById(Long id);
}
