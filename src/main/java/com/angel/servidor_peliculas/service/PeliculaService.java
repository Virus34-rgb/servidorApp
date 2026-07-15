package com.angel.servidor_peliculas.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.angel.servidor_peliculas.dto.PeliculaResponse;
import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.repository.PeliculaRepository;
@Service 
public class PeliculaService implements IPeliculaService{
	
	private PeliculaRepository repository;
	
	private static final Logger logger = LoggerFactory.getLogger(PeliculaService.class);

	public PeliculaService(PeliculaRepository repository) {
		this.repository = repository;
	}
	@Override
	public ArrayList<Pelicula> getAll() {
		return new ArrayList<>(repository.findAll());
	}

	@Override
	public Pelicula getById(long id) {
		if(repository.existsById(id)) {
			logger.info("La pelicula con id " + id + " se encuentra en el sistema");
			return repository.findById(id).get();
		}
		logger.info("La pelicula con id " + id + " no se encuentra en el sistema");
		return null;
	}

	@Override
	public boolean add(Pelicula pelicula) {
		if(!repository.existsByNombreAndFecha(pelicula.getNombre(), pelicula.getFecha())) {
			logger.info("La pelicula  no se encuentra en el sistema, procedemos a añadirla");
			repository.save(pelicula);
			return true;
		}
		else {
			logger.info("La pelicula se encuentra en el sistema, no se puede añadir");
			return false;
		}
		
	}

	@Override
	public boolean delete(long id) {
		if(repository.existsById(id)) {
			logger.info("La pelicula se encuentra en el sistema, procedemos a borrarla");
			repository.deleteById(id);;
			return true;
		}
		else {
			logger.info("La pelicula no se encuentra en el sistema, no se puede borrar");
			return false;
		}
	}

	@Override
	public boolean update(Pelicula pelicula) {
		if(repository.existsById(pelicula.getId())) {
			logger.info("La pelicula  se encuentra en el sistema, procedemos a actualizarla");
			repository.save(pelicula);
			return true;
		}
		else {
			logger.info("La pelicula no se encuentra en el sistema, no se puede actualizar");
			return false;
		}
		
	}
	@Override
	public Optional<PeliculaResponse> buscarPorTmdbId(Long tmdbId) {
		Optional<Pelicula> pelicula = repository.findByTmdbId(tmdbId);
		if(pelicula.isPresent()) {
			Pelicula pel = pelicula.get();
			PeliculaResponse res = new PeliculaResponse(pel.getId(),pel.getTmdbId(),pel.getNombre(),pel.getGenero()
					,pel.getFecha(),pel.getPosterPath());
			return Optional.of(res);
		}
		else {
			return Optional.empty();
		}
	}
}
