package com.angel.servidor_peliculas.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.angel.servidor_peliculas.dto.ResenaPublicaResponse;
import com.angel.servidor_peliculas.dto.ResenaRequest;
import com.angel.servidor_peliculas.dto.ResenaResponse;
import com.angel.servidor_peliculas.service.IResenaService;

@RestController
@RequestMapping("/resenas")
public class ResenaController {
	private IResenaService resenaService;
	public ResenaController(IResenaService resenaService) {
		this.resenaService = resenaService;
	}
	@GetMapping("/{id}")
	public ResponseEntity<ResenaResponse> obtener(@PathVariable Long id) {
	    return ResponseEntity.ok(resenaService.obtenerResena(id));
	}
	@PostMapping
	public ResponseEntity<ResenaResponse> crear(@RequestBody ResenaRequest request) {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    ResenaResponse response = resenaService.crearResena(username, request);
	    return ResponseEntity.ok(response);
	}
	@GetMapping
	public ResponseEntity<List<ResenaResponse>> listar(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<ResenaResponse> lista = resenaService.listarMisResenas(username);
		return ResponseEntity.ok(lista);
	}
	@PutMapping("/{id}")
	public ResponseEntity<ResenaResponse> actualizar(@PathVariable Long id, @RequestBody ResenaRequest request){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ResenaResponse response = resenaService.actualizarResena(username, id, request);
		return ResponseEntity.ok(response);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> borrar(@PathVariable Long id){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		resenaService.borrarResena(username, id);
		return ResponseEntity.noContent().build();
	}
	@GetMapping("/filtered")
	public ResponseEntity<List<ResenaResponse>> listarFiltrado(
	        @RequestParam String filtro,
	        @RequestParam boolean ascendente) {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    return ResponseEntity.ok(resenaService.listarMisResenasFiltradas(username, filtro, ascendente));
	}
	@GetMapping("/pelicula/{peliculaId}")
	public ResponseEntity<List<ResenaPublicaResponse>> resenasDePelicula(@PathVariable Long peliculaId) {
	    return ResponseEntity.ok(resenaService.listarResenasDePelicula(peliculaId));
	}
}
