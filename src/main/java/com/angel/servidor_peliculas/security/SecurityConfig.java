package com.angel.servidor_peliculas.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration 
@EnableWebSecurity
public class SecurityConfig {
	private JwtFilter jwtFilter;
	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	@Bean UserDetailsService userDetailsService() {
	    return username -> {
	        throw new UsernameNotFoundException("No user details service");
	    };
	}
	@Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			    .csrf(csrf -> csrf.disable())
			    .httpBasic(httpBasic -> httpBasic.disable())
			    .formLogin(formLogin -> formLogin.disable())
			    .sessionManagement(session -> session
			        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			    .authorizeHttpRequests(auth -> auth
			        .requestMatchers("/auth/**").permitAll()
			        .anyRequest().authenticated()
			    )
			    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			    .build();
	}
}
