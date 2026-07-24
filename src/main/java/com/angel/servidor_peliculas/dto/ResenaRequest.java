package com.angel.servidor_peliculas.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaRequest {
	private Long tmdbId;
	private String nombre;
	private List<String> generos;
	private LocalDate fecha;
	private String posterPath;
	private double nota;
	private String opinion;
}
