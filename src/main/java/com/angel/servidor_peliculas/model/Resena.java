package com.angel.servidor_peliculas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "pelicula_id"}))
@Entity
public class Resena {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name = "pelicula_id")
	private Pelicula pelicula;
	private double nota;
	@Column(nullable = true)
	private String opinion;
	private LocalDate fechaPublicacion;
	
}
