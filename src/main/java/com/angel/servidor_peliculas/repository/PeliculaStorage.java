package com.angel.servidor_peliculas.repository;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.angel.servidor_peliculas.model.Pelicula;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
@Component
public class PeliculaStorage implements IPeliculaStorage{
	@Value("${storage.ruta}")
	private String ruta;
	private Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
		        public JsonElement serialize(LocalDate src, Type type, JsonSerializationContext context) {
		            return new JsonPrimitive(src.toString());
		        }
		    })
		    .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
		        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		            return LocalDate.parse(json.getAsString());
		        }
		    })
		    .create();
	private static final Logger logger = LoggerFactory.getLogger(PeliculaStorage.class);
	@Override
	public void guardar(HashMap<Long, Pelicula> mapa) {
		new File(ruta).getParentFile().mkdirs();
		try (FileWriter writer = new FileWriter(ruta)) {
		    gson.toJson(mapa, writer);
		    logger.info("Se ha guardado correctamente el mapa de las peliculas");
		}catch (IOException e) {
		    e.printStackTrace();
		    logger.error("Ha ocurrido un error al intentar abrir el archivo");
		}
	}

	@Override
	public HashMap<Long, Pelicula> cargar() {
		HashMap<Long,Pelicula> mapa;
		File file = new File(ruta);
		if (!file.exists() || file.length() == 0) {
		    return new HashMap<>();
		}
		try (FileReader reader = new FileReader(ruta)) {
		    Type tipo = new TypeToken<HashMap<Long,Pelicula>>(){}.getType();
		    mapa = gson.fromJson(reader, tipo);
			if (mapa == null) {
			    mapa = new HashMap<Long,Pelicula>();
			    logger.info("No existia información anterior, se ha creado el mapa");
			}
			else {
				logger.info("Se ha cargado correctamente el mapa de las peliculas");
			}
			return mapa;
		} catch (IOException e) {
			logger.error("Ha ocurrido un error al intentar abrir el archivo");
			mapa = new HashMap<Long,Pelicula>();
		    return mapa;
		}
	}
}
