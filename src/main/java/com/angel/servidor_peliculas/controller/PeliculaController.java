package com.angel.servidor_peliculas.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.angel.servidor_peliculas.dto.PeliculaResponse;
import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.service.IPeliculaService;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {
    private final IPeliculaService service;

    PeliculaController(IPeliculaService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<ArrayList<Pelicula>> getAll() {
        ArrayList<Pelicula> peliculas = service.getAll();
        return ResponseEntity.ok(peliculas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> getById(@PathVariable long id){
    	Pelicula pelicula = service.getById(id);
    	if(pelicula == null) {
    		return ResponseEntity.notFound().build();
    	}
    	else {
    		return ResponseEntity.ok(pelicula);
    	}
    	
    }

    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<PeliculaResponse> buscarPorTmdb(@PathVariable Long tmdbId) {
        return service.buscarPorTmdbId(tmdbId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
