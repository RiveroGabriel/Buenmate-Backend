package com.example.buenmate.authsecurity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

// DTO que se encargar de devoler el token al cliente despues del login o registro
public class AuthResponse {
    private String token;
}
