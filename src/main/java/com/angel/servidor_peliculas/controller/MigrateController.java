package com.angel.servidor_peliculas.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.model.Resena;
import com.angel.servidor_peliculas.model.Usuario;
import com.angel.servidor_peliculas.repository.ResenaRepository;
import com.angel.servidor_peliculas.repository.UsuarioRepository;
import com.angel.servidor_peliculas.security.AuthController;
import com.angel.servidor_peliculas.service.IPeliculaService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
@RestController
@RequestMapping
public class MigrateController {
	IPeliculaService service;
	UsuarioRepository usuarioRepository;
	ResenaRepository resenaRepository;
	private static final Logger logger = LoggerFactory.getLogger(MigrateController.class);
    MigrateController(IPeliculaService service,UsuarioRepository usuarioRepository,ResenaRepository resenaRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.resenaRepository = resenaRepository;
    }
	@GetMapping("/migrate/resenas")
	public ResponseEntity<Void> migrarResenas() {
	    // 1. Buscar el usuario una sola vez, fuera del bucle
	    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername("angel34");
	    if (usuarioOpt.isEmpty()) {
	        return ResponseEntity.badRequest().build();
	    }
	    Usuario usuario = usuarioOpt.get();

	    List<Pelicula> lista = service.getAll();
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "https://api.themoviedb.org/3/search/movie?query={nombre}&api_key={key}&language=es-ES";

	    for (Pelicula p : lista) {
	        if (p.getTmdbId() != null) continue; // ya migrada

	        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, p.getNombre(), "01dfdd72454756120c853314fbe5a9a8");
	        JsonObject json = new Gson().fromJson(response.getBody(), JsonObject.class);
	        JsonArray results = json.getAsJsonArray("results");

	        Long tmdbIdEncontrado = null;
	        for (int i = 0; i < results.size(); i++) {
	            JsonObject resultado = results.get(i).getAsJsonObject();
	            String releaseDate = resultado.get("release_date").getAsString();
	            try {
	                LocalDate fechaTmdb = LocalDate.parse(releaseDate);
	                if (fechaTmdb.getYear() == p.getFecha().getYear()) {
	                    tmdbIdEncontrado = resultado.get("id").getAsLong();
	                    break;
	                }
	            } catch (Exception e) {
	                continue; // release_date vacío o mal formado, siguiente resultado
	            }
	        }

	        if (tmdbIdEncontrado == null) {
	            logger.warn("No se encontró tmdbId para: {}", p.getNombre());
	            continue;
	        }

	        p.setTmdbId(tmdbIdEncontrado);
	        service.update(p);

	        Resena resena = new Resena(null, usuario, p, p.getNota(), null, LocalDate.now());
	        resenaRepository.save(resena);
	    }
	    return ResponseEntity.ok(null);
	}
}
