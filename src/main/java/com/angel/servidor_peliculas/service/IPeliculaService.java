package com.angel.servidor_peliculas.service;

import java.util.ArrayList;
import java.util.Optional;

import com.angel.servidor_peliculas.dto.PeliculaResponse;
import com.angel.servidor_peliculas.model.Pelicula;

public interface IPeliculaService {
    ArrayList<Pelicula> getAll();
    Pelicula getById(long id);
    boolean add(Pelicula pelicula);
    boolean delete(long id);
    boolean update(Pelicula pelicula);
    Optional<PeliculaResponse> buscarPorTmdbId(Long tmdbId);
}
