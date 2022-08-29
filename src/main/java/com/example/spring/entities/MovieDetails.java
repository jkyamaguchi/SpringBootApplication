package com.example.spring.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor 
public class MovieDetails{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String overview; //summary
	@Enumerated(EnumType.ORDINAL)
	@ElementCollection(targetClass = Genre.class)
	@Column(name="genres")
	private List<Genre> genres;
	
	@OneToOne(mappedBy = "details")
	private Movie movie;

}
