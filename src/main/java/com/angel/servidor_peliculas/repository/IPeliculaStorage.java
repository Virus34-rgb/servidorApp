package com.angel.servidor_peliculas.repository;

import java.util.HashMap;

import com.angel.servidor_peliculas.model.Pelicula;

public interface IPeliculaStorage {
	public void guardar(HashMap<Long, Pelicula> mapa);
	public HashMap<Long, Pelicula> cargar();
}
