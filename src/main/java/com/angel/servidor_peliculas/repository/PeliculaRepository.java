package com.angel.servidor_peliculas.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.angel.servidor_peliculas.model.Pelicula;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PeliculaRepository implements IPeliculaRepository{
	private HashMap<Long, Pelicula> peliculas = new HashMap<>();
	private long nextId;
    private final IPeliculaStorage storage;
    private static final Logger logger = LoggerFactory.getLogger(PeliculaRepository.class);

	private PeliculaRepository(IPeliculaStorage storage) {
		this.storage = storage;
	}
    
    @PostConstruct
    public void init() {
        peliculas = storage.cargar();
        nextId = peliculas.values().stream()
            .mapToLong(Pelicula::getId)
            .max()
            .orElse(0) + 1;
    }
	@Override
	public ArrayList<Pelicula> getAll() {
		return new ArrayList<>(peliculas.values());
	}

	@Override
	public Pelicula getById(long id) {
		return peliculas.get(id);
	}

	@Override
	public void add(Pelicula pelicula) {
		pelicula.setId(nextId);
		peliculas.put(nextId, pelicula);
		logger.info("Se ha añadido la pelicula : " + pelicula.getNombre() + " con ID " + pelicula.getId());
		nextId++;
		storage.guardar(peliculas);
	}

	@Override
	public void delete(long id) {
		if(peliculas.containsKey(id)) {
			peliculas.remove(id);
			logger.info("Se ha borrado la pelicula con id : " + id);
			storage.guardar(peliculas);
		}
		else {
			logger.warn("No existe una pelicula con id " + id + " enj el mapa actual");
		}
	}

	@Override
	public void update(Pelicula pelicula) {
		long id = pelicula.getId();
		peliculas.put(id, pelicula);
		storage.guardar(peliculas);
		logger.info("Se ha actualizado la pelicula : " + pelicula.getNombre() + " con ID " + pelicula.getId());
	}
	

	@Override
	public boolean existsById(long id) {
		return peliculas.containsKey(id);
	}

	@Override
	public boolean existsByNombreAndFecha(String nombre, LocalDate fecha) {
		for(Pelicula p : peliculas.values()) {
			if(nombre.equals(p.getNombre()) && fecha.equals(p.getFecha())) {
				return true;
			}
		}
		return false;
	}
	
}
