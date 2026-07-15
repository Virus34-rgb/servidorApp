package com.angel.servidor_peliculas.service;

import java.util.List;

import com.angel.servidor_peliculas.dto.ResenaPublicaResponse;
import com.angel.servidor_peliculas.dto.ResenaRequest;
import com.angel.servidor_peliculas.dto.ResenaResponse;

public interface IResenaService {
	ResenaResponse obtenerResena(Long resenaId);
    ResenaResponse crearResena(String username, ResenaRequest request);
    List<ResenaResponse> listarMisResenas(String username);
    ResenaResponse actualizarResena(String username, Long resenaId, ResenaRequest request);
    void borrarResena(String username, Long resenaId);
    List<ResenaResponse> listarMisResenasFiltradas(String username, String filtro, boolean ascendente);
    List<ResenaPublicaResponse> listarResenasDePelicula(Long peliculaId);
}
