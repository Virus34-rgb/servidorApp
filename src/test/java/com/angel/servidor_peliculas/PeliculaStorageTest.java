package com.angel.servidor_peliculas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.repository.IPeliculaRepository;
import com.angel.servidor_peliculas.repository.IPeliculaStorage;
@SpringBootTest
public class PeliculaStorageTest {
	@Autowired
    private IPeliculaRepository repository;
	@Autowired
    private IPeliculaStorage storage;
    Pelicula film1;
    Pelicula film2;
    Pelicula film3;
	@BeforeEach
	public void setting() {
		ArrayList<String> lista = new ArrayList<>();
		lista.add("Suspense");
		ArrayList<String> lista2 = new ArrayList<>();
		lista2.add("Drama Histórico");
		film1 = new Pelicula(0,"El Silencio de los Corderos",10,lista,LocalDate.of(1991, 9, 6));
		film2 = new Pelicula(0,"Seven",10,lista,LocalDate.of(1995, 1, 10));
		film3 = new Pelicula(0,"La lista de Schindler",10,lista2,LocalDate.of(1993, 11, 30));
	}
	@AfterEach
	public void remove() {
		ArrayList<Pelicula> peliculas = repository.getAll();
		for(Pelicula p : peliculas) {
			repository.delete(p.getId());
		}
	}
	@Test
	@DirtiesContext
	public void cargarGuardarTest() {
		film1.setId(1L);
		film2.setId(2L);
	    HashMap<Long, Pelicula> mapaOriginal = new HashMap<>();
	    mapaOriginal.put(1L, film1);
	    mapaOriginal.put(2L, film2);
	    
	    storage.guardar(mapaOriginal);
	    HashMap<Long, Pelicula> mapaCargado = storage.cargar();
	    
	    assertEquals(mapaOriginal, mapaCargado);
	}

	@Test
	@DirtiesContext
	public void cargarFicheroInexistenteTest() throws IOException {
	    Files.deleteIfExists(Paths.get("data/peliculas.json"));
	    HashMap<Long, Pelicula> mapa = storage.cargar();
	    assertTrue(mapa.isEmpty());
	}
}
