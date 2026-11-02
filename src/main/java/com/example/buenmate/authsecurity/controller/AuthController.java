package com.example.buenmate.authsecurity.controller;

import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.authsecurity.dto.AuthRequest;
import com.example.buenmate.authsecurity.dto.AuthResponse;
import com.example.buenmate.authsecurity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
// Expone los endpoints de registro y login
public class AuthController {

    private final AuthService authService; // inyectamos el servicio
    private final UsuarioRepository usuarioRepository;

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

    // GET /api/v1/auth/me → devuelve los datos del usuario logueado
    @GetMapping("/datosusuario")
    public ResponseEntity<Map<String, Object>> datosusuario(Authentication authentication) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombre", usuario.getNombre());
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol().name());

        return ResponseEntity.ok(response);
    }


}
