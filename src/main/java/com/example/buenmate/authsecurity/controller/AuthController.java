package com.example.buenmate.authsecurity.controller;

import com.example.buenmate.authsecurity.dto.AuthRequest;
import com.example.buenmate.authsecurity.dto.AuthResponse;
import com.example.buenmate.authsecurity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
// Expone los endpoints de registro y login
public class AuthController {

    private final AuthService authService; // inyectamos el servicio

    // POST /api/v1/auth/register → registra un nuevo usuario y devuelve token
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    // POST /api/v1/auth/login → autentica un usuario y devuelve token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
