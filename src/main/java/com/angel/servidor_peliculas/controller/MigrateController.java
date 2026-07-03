package com.angel.servidor_peliculas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.service.IPeliculaService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
@RestController
@RequestMapping
public class MigrateController {
	IPeliculaService service;
    MigrateController(IPeliculaService service) {
        this.service = service;
    }
	@GetMapping("/migrate/posters")
    public ResponseEntity<Void> actualizar(){
		List<Pelicula> lista = service.getAll();
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.themoviedb.org/3/search/movie?query={nombre}&api_key={key}&language=es-ES";
		for (Pelicula p : lista) {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, p.getNombre(), "01dfdd72454756120c853314fbe5a9a8");
			JsonObject json = new Gson().fromJson(response.getBody(), JsonObject.class);
			JsonArray results = json.getAsJsonArray("results");
			if (results.size() > 0) {
			    String posterPath = results.get(0).getAsJsonObject().get("poster_path").getAsString();
			    p.setPosterPath(posterPath);
			    service.update(p);
			}
		}
		return ResponseEntity.ok(null);
    }
}
