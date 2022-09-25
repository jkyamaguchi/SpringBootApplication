package com.example.spring.entities;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL) //do not return property if it is null
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
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) //if Movie is deleted, then its child details is also deleted
	@JoinColumn(name = "movie_details_id", referencedColumnName = "id")
	private MovieDetails details;
	
	
}
