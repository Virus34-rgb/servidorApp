package com.angel.servidor_peliculas.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pelicula {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(unique = true)
    private Long tmdbId;
    @ElementCollection
    private List<String> genero;
    private LocalDate fecha;
    private String posterPath;
}