package com.angel.servidor_peliculas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.angel.servidor_peliculas.dto.ResenaRequest;
import com.angel.servidor_peliculas.dto.ResenaResponse;
import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.model.Resena;
import com.angel.servidor_peliculas.model.Usuario;
import com.angel.servidor_peliculas.repository.PeliculaRepository;
import com.angel.servidor_peliculas.repository.ResenaRepository;
import com.angel.servidor_peliculas.repository.UsuarioRepository;
@Service
public class ResenaService implements IResenaService{
	private PeliculaRepository peliculaRepository;
	private ResenaRepository resenaRepository;
	private UsuarioRepository usuarioRepository;
	private static final Logger logger = LoggerFactory.getLogger(ResenaService.class);
	public ResenaService(PeliculaRepository peliculaRepository, ResenaRepository resenaRepository,
			UsuarioRepository usuarioRepository) {
		this.peliculaRepository = peliculaRepository;
		this.resenaRepository = resenaRepository;
		this.usuarioRepository = usuarioRepository;
	}
	@Override
	public ResenaResponse obtenerResena(Long resenaId) {
		Optional<Resena> res = resenaRepository.findById(resenaId);
		if(res.isEmpty()) {
			logger.error("Reseña no disponible en el sistema");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada");
		}
		Resena resena = res.get();
		return aResponse(resena);
	}
	@Transactional
	@Override
	public ResenaResponse crearResena(String username, ResenaRequest request) throws ResponseStatusException {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if(usuario.isEmpty()) {
			logger.error("El usuario {} , no esta presente en el sistema",username);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
		Usuario user = usuario.get();
		Pelicula film;
		if (request.getTmbdId() != null) {
		    Optional<Pelicula> pelicula = peliculaRepository.findByTmdbId(request.getTmbdId());
		    if (pelicula.isPresent()) {
		        film = pelicula.get();
		    } else {
		        film = new Pelicula(null, request.getNombre(), request.getTmbdId(), request.getGeneros(), request.getFecha(), request.getPosterPath());
		        peliculaRepository.save(film);
		    }
		} else {
		    film = new Pelicula(null, request.getNombre(), null, request.getGeneros(), request.getFecha(), request.getPosterPath());
		    peliculaRepository.save(film);
		}
		if(resenaRepository.findByUsuarioIdAndPeliculaId(user.getId(), film.getId()).isPresent()) {
			logger.error("Ya existe una reseña para la pelicula {} del usuario {} ", user.getUsername(),film.getNombre());
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una reseña tuya para esta película");
		}
		LocalDate fechaAhora = LocalDate.now();
		Resena resena = new Resena(null,user,film,
				request.getNota(),request.getOpinion(),fechaAhora);
		resenaRepository.save(resena);
		logger.info("Reseña registrada en el sistema correctamente");
		return aResponse(resena);
	}

	@Override
	public List<ResenaResponse> listarMisResenas(String username) throws ResponseStatusException {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if(usuario.isEmpty()) {
			logger.error("El usuario {} , no esta presente en el sistema");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
		Usuario user = usuario.get();
		List<Resena> lista = resenaRepository.findByUsuarioId(user.getId());
		List<ResenaResponse> response = new ArrayList<>();
		for(Resena resena : lista) {
			response.add(aResponse(resena));
		}
		logger.info("Lista de peliculas devuelta correctamente");
		return response;
	}
	@Transactional
	@Override
	public ResenaResponse actualizarResena(String username, Long resenaId, ResenaRequest request) throws ResponseStatusException {
		Optional<Resena> res = resenaRepository.findById(resenaId);
		if(res.isEmpty()) {
			logger.error("Reseña no disponible en el sistema");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada");
		}
		Resena resena = res.get();
		if(!resena.getUsuario().getUsername().equals(username)) {
			logger.error("El usuario no puede modificar una reseña que no ha sido escrita por el");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La reseña no puede ser alterada por el usuario");
		}
		resena.setNota(request.getNota());
		resena.setOpinion(request.getOpinion());
		resenaRepository.save(resena);
		logger.info("Reseña actualizada correctamente");
		return aResponse(resena);
	}

	@Override
	public void borrarResena(String username, Long resenaId) throws ResponseStatusException {
		Optional<Resena> res = resenaRepository.findById(resenaId);
		if(res.isEmpty()) {
			logger.error("Reseña no disponible en el sistema");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada");
		}
		Resena resena = res.get();
		if(!resena.getUsuario().getUsername().equals(username)) {
			logger.error("El usuario no puede modificar una reseña que no ha sido escrita por el");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La reseña no puede ser alterada por el usuario");
		}
		logger.info("Reseña borrada correctamente");
		resenaRepository.deleteById(resenaId);	
	}
	private ResenaResponse aResponse(Resena resena) {
	    Pelicula film = resena.getPelicula();
	    return new ResenaResponse(resena.getId(), film.getId(), film.getTmdbId(),
	            film.getNombre(), film.getGenero(), film.getFecha(), film.getPosterPath(),
	            resena.getNota(), resena.getOpinion(), resena.getFechaPublicacion());
	}
	@Override
	public List<ResenaResponse> listarMisResenasFiltradas(String username, String filtro, boolean ascendente) {
	    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
	    if (usuarioOpt.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	    }
	    Usuario usuario = usuarioOpt.get();
	    List<Resena> lista = resenaRepository.findByUsuarioId(usuario.getId());
	    Comparator<Resena> c;
	    switch (filtro) {
	        case "nombre":
	            c = Comparator.comparing(r -> r.getPelicula().getNombre());
	            break;
	        case "nota":
	            c = Comparator.comparingDouble(Resena::getNota);
	            break;
	        case "fecha":
	            c = Comparator.comparing(r -> r.getPelicula().getFecha());
	            break;
	        default:
	            c = Comparator.comparing(Resena::getId);
	            break;
	    }
	    if (!ascendente) {
	        c = c.reversed();
	    }

	    return lista.stream()
	            .sorted(c)
	            .map(this::aResponse)
	            .collect(Collectors.toList());
	}
}
