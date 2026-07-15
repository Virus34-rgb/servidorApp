package com.angel.servidor_peliculas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.angel.servidor_peliculas.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena,Long> {
	@Query("SELECT r FROM Resena r JOIN FETCH r.pelicula p LEFT JOIN FETCH p.genero WHERE r.usuario.id = :usuarioId")
	List<Resena> findByUsuarioIdConPelicula(@Param("usuarioId") Long usuarioId);

	Optional<Resena> findByUsuarioIdAndPeliculaId(Long usuarioId, Long peliculaId);
}