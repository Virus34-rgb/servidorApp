package com.angel.servidor_peliculas.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angel.servidor_peliculas.model.Usuario;
import com.angel.servidor_peliculas.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private JwtUtil jwtUtil;
	private UsuarioRepository usuarioRepository;
	private PasswordEncoder passwordEncoder;
	public AuthController(JwtUtil jwtUtil,UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder) {
		this.jwtUtil = jwtUtil;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginData data){
		logger.info("Login attempt");
		String user = data.getUsername();
		String pass = data.getPassword();
		Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(user);
		if (usuarioEncontrado.isEmpty()) {
		    logger.error("Usuario : {} incorrecto, registrate", user);
		    return ResponseEntity.badRequest().build();
		}
		if (!passwordEncoder.matches(pass, usuarioEncontrado.get().getPasswordHash())) {
		    logger.error("Contraseña incorrecta para el usuario: {}", user);
		    return ResponseEntity.badRequest().build();
		}
		String token = jwtUtil.generateToken(user);
		logger.info("Token generado: {}", token);
		return ResponseEntity.ok(new AuthResponse(token));
	}
	@PostMapping("/register")
	public ResponseEntity<String> register (@RequestBody LoginData data){
		logger.info("Register attempt");
		String user = data.getUsername();
		String pass = data.getPassword();
		if(usuarioRepository.findByUsername(user).isPresent()) {
			logger.error("Usuario : {} ya registrado", user);
			return ResponseEntity.badRequest().build();
		}
		else {
			logger.info("Añadimos al usuario : {} a la lista de usuarios registrados", user);
			usuarioRepository.save(new Usuario(null,user,passwordEncoder.encode(pass)));
			return ResponseEntity.ok("");
		}
	}
}
