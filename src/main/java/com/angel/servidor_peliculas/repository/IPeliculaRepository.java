package com.angel.servidor_peliculas.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.angel.servidor_peliculas.model.Pelicula;

public interface IPeliculaRepository {
    ArrayList<Pelicula> getAll();
    Pelicula getById(long id);
    void add(Pelicula pelicula);
    void delete(long id);
    void update(Pelicula pelicula);
    boolean existsById(long id);
    boolean existsByNombreAndFecha(String nombre, LocalDate fecha);
}