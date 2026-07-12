package com.angel.servidor_peliculas.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.angel.servidor_peliculas.repository.UsuarioRepository;

import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	JwtUtil jwtUtil;
	private final UsuarioRepository usuarioRepository;
	public JwtFilter(JwtUtil jwtUtil,UsuarioRepository usuarioRepository) {
		this.jwtUtil = jwtUtil;
		this.usuarioRepository = usuarioRepository;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("Request URI: {}", request.getRequestURI());
		try {
			String header = request.getHeader("Authorization");
			if (request.getRequestURI().startsWith("/auth/")) {
			    filterChain.doFilter(request, response);
			    return;
			}
			if (header == null || !header.startsWith("Bearer ")) {
				logger.error("Header null");
				response.setStatus(401);
				return;
			}
			String username = jwtUtil.extractUsername(header.substring(7));
			if(!jwtUtil.isTokenValid(header.substring(7))){
				logger.error("TOKEN NO VALIDO");
				response.setStatus(401);
				return;
			}
			else if(usuarioRepository.findByUsername(username).isEmpty()) {
				logger.error("USUARIO NO VALIDO");
				response.setStatus(401);
				return;
			}
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				    username, null, Collections.emptyList()
				);
				SecurityContextHolder.getContext().setAuthentication(auth);
			filterChain.doFilter(request, response);
			
		}
		catch(Exception e){
			logger.error("ERROR: {}", e.getMessage());
			response.setStatus(401);
			return;
		}
	}
}
