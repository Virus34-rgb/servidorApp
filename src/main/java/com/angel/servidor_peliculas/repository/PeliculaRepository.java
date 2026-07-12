package com.angel.servidor_peliculas.repository;



import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angel.servidor_peliculas.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
	boolean existsByNombreAndFecha(String nombre, LocalDate fecha);
	Optional<Pelicula> findByTmdbId(Long tmdbId);
}