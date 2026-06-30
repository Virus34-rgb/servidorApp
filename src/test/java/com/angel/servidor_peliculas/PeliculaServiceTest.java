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
import com.angel.servidor_peliculas.service.IPeliculaService;

@SpringBootTest
public class PeliculaServiceTest {

    @Autowired
    private IPeliculaService service;

    Pelicula film1;
    Pelicula film2;

    @BeforeAll
    static void limpiarFichero() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/peliculas.json"));
    }

    @BeforeEach
    public void setting() {
		ArrayList<String> lista = new ArrayList<>();
		lista.add("Suspense");
        film1 = new Pelicula((long) 0, "El Silencio de los Corderos", 10, lista, LocalDate.of(1991, 9, 6));
        film2 = new Pelicula((long) 0, "Seven", 10, lista, LocalDate.of(1995, 1, 10));
    }

    @AfterEach
    public void remove() {
        ArrayList<Pelicula> peliculas = service.getAll();
        for (Pelicula p : peliculas) {
            service.delete(p.getId());
        }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addSinDuplicadoTest() {
        service.add(film1);
        assertEquals(1, service.getAll().size());
        assertEquals(film1.getNombre(), service.getById(1).getNombre());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addConDuplicadoTest() {
        service.add(film1);
        service.add(film1);
        assertEquals(1, service.getAll().size());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteExistenteTest() {
        service.add(film1);
        service.delete(1);
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteInexistenteTest() {
        service.add(film1);
        service.delete(999);
        assertEquals(1, service.getAll().size());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateExistenteTest() {
        service.add(film1);
		ArrayList<String> lista = new ArrayList<>();
		lista.add("Suspense");
        Pelicula actualizada = new Pelicula((long) 1, "El Silencio de los Corderos", 7, lista, LocalDate.of(1991, 9, 6));
        service.update(actualizada);
        assertEquals(7, service.getById(1).getNota());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateInexistenteTest() {
		ArrayList<String> lista2 = new ArrayList<>();
		lista2.add("Drama");
        Pelicula inexistente = new Pelicula((long) 999, "Inexistente", 5, lista2, LocalDate.of(2000, 1, 1));
        service.update(inexistente);
        assertTrue(service.getAll().isEmpty());
    }
}