package com.angel.servidor_peliculas.model;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pelicula {
    private long id;
    private String nombre;
    private double nota;
    private ArrayList<String> genero;
    private LocalDate fecha;
}