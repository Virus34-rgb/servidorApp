package com.angel.servidor_peliculas.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Value("${app.usuario}")
	private String usuario;
	@Value("${app.password}")
	private String password;
	private JwtUtil jwtUtil;
	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginData data){
		logger.info("Login attempt");
		String user = data.getUsername();
		String pass = data.getPassword();
		if(!user.trim().equals(usuario)) {
			logger.error("Usuario : {} incorrecto", user);
			return ResponseEntity.badRequest().build();
		}
		else if(!pass.equals(password)) {
			logger.error("Contraseña : {} incorrecta", pass);
			return ResponseEntity.badRequest().build();
		}
		String token = jwtUtil.generateToken(user);
		logger.info("Token generado: {}", token);
		return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user)));
	}

}
