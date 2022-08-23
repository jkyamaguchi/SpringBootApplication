package com.example.spring.entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor //for the call to super() constructor made by inheritance
@Getter
@Setter
@ToString
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@JsonProperty("image_path")
	private String imagePath;//image address
	private String title;
	@JsonProperty("release_date")
	private String releaseDate;
	@JsonProperty("vote_average")
	private Double voteAverage;
}
