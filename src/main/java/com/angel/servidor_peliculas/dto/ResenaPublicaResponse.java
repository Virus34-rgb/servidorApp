package com.angel.servidor_peliculas.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaPublicaResponse {
    private Long resenaId;
    private String username;
    private double nota;
    private String opinion;
    private LocalDate fechaPublicacion;
}
