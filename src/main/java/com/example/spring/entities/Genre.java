package com.example.spring.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Genre {
	ACTION("Action"),
	COMEDY("Comedy"),
	DRAMA("Drama"),
	FANTASY("Fantasy"),
	HORROR("Horror"),
	MYSTERY("Mistery"),
	ROMANCE("Romance"),
	THRILLER("Thriller");
	
	@Getter
	private String name;
}
