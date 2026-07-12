package com.angel.servidor_peliculas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angel.servidor_peliculas.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena,Long> {
	List<Resena> findByUsuarioId(Long usuarioId);
	Optional<Resena> findByUsuarioIdAndPeliculaId(Long usuarioId, Long peliculaId);
}
