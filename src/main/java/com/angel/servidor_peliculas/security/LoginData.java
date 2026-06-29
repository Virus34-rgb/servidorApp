package com.angel.servidor_peliculas.security;

import lombok.Data;

@Data
public class LoginData {
    private String username;
    private String password;
}