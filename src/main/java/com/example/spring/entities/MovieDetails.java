package com.example.spring.entities;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class MovieDetails extends Movie{
	private String overview; //summary 
	@ElementCollection
	private List<Genre> genres;
	

}
