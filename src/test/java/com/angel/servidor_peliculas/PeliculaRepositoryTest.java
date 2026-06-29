package com.angel.servidor_peliculas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.angel.servidor_peliculas.model.Pelicula;
import com.angel.servidor_peliculas.repository.IPeliculaRepository;

@SpringBootTest
public class PeliculaRepositoryTest {
    @Autowired
    private IPeliculaRepository repository;
    Pelicula film1;
    Pelicula film2;
    Pelicula film3;
	@BeforeEach
	public void setting() throws IOException {
		ArrayList<String> lista = new ArrayList<>();
		lista.add("Suspense");
		ArrayList<String> lista2 = new ArrayList<>();
		lista2.add("Drama Histórico");
		film1 = new Pelicula(0,"El Silencio de los Corderos",10,lista,LocalDate.of(1991, 9, 6));
		film2 = new Pelicula(0,"Seven",10,lista,LocalDate.of(1995, 1, 10));
		film3 = new Pelicula(0,"La lista de Schindler",10,lista2,LocalDate.of(1993, 11, 30));
	}
	@BeforeAll
	static void limpiarFichero() throws IOException {
	    Files.deleteIfExists(Paths.get("src/test/resources/peliculas.json"));
	}
	@AfterEach
	public void remove() {
		ArrayList<Pelicula> peliculas = repository.getAll();
		for(Pelicula p : peliculas) {
			repository.delete(p.getId());
		}
	}
	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void addTesting() {
		ArrayList<Pelicula> lista = new ArrayList<>();
		repository.add(film1);
		lista.add(film1);
		assertEquals(repository.getAll(),lista);
		repository.add(film2);
		lista.add(film2);
		assertEquals(repository.getAll(),lista);
		assertEquals(film2.getId(),2);
	}
	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void deleteTesting() {
		ArrayList<Pelicula> lista = new ArrayList<>();
		repository.add(film1);
		repository.add(film2);
		repository.add(film3);
		lista.add(film1);
		lista.add(film2);
		lista.add(film3);
		assertEquals(repository.getAll(),lista);
		repository.delete(1);
		lista.remove(film1);
		assertEquals(repository.getAll(),lista);
		lista.remove(film2);
		repository.delete(2);
		assertEquals(repository.getAll(),lista);
	}
	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void updateTesting() {
		repository.add(film1);
		ArrayList<String> lista = new ArrayList<>();
		lista.add("Suspense");
		Pelicula p1 = new Pelicula (1,"El Silencio de los Corderos",7,lista,LocalDate.of(1991, 9, 6));
		repository.update(p1);
		assertEquals(repository.getAll().get(0).getNota(),7);
		repository.delete(1);
	}
}