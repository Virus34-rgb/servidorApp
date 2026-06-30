package com.angel.servidor_peliculas.repository;



import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angel.servidor_peliculas.model.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
	boolean existsByNombreAndFecha(String nombre, LocalDate fecha);
}