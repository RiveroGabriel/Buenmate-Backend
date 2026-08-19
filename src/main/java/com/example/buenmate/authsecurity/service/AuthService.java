package com.example.buenmate.authsecurity.service;

import com.example.buenmate.authsecurity.dto.AuthRequest;
import com.example.buenmate.authsecurity.dto.AuthResponse;

public interface AuthService {

    // Registra un nuevo usuario y devuelve un token
    AuthResponse register(AuthRequest request);

    // Autentica un usuario existente y devuelve un token
    AuthResponse authenticate(AuthRequest request);
}

