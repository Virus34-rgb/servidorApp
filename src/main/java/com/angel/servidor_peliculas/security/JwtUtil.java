package com.angel.servidor_peliculas.security;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private Long expiration;
	public String generateToken(String username) {
		return Jwts.builder()
	    .subject(username)
	    .signWith(getKey())
	    .expiration(new Date(System.currentTimeMillis() + expiration))
	    .compact();
	}
	public String extractUsername (String jwts) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jwts).getPayload().getSubject();
	}
	
	private SecretKey getKey() {
	    return Keys.hmacShaKeyFor(secret.getBytes());
	}
	public boolean isTokenValid(String s) {
		try {
		    Jwts.parser().verifyWith(getKey()).build().parse(s);
		    return true;
		} catch(Exception e) {
		   return false;
		}
	}
}
