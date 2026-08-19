package com.example.buenmate.authsecurity.service;

import com.example.buenmate.Enums.Rol;
import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.authsecurity.dto.AuthRequest;
import com.example.buenmate.authsecurity.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// Implementa la interfaz AuthService con la lógica real de registro y login

public class AuthServiceImpl implements AuthService {

    // Inyecciones: Spring nos provee estas dependencias automáticamente
    private final UsuarioRepository usuarioRepository; // para guardar y buscar usuarios en la BD
    private final PasswordEncoder passwordEncoder;     // para encriptar contraseñas
    private final JwtService jwtService;               // para generar el token
    private final AuthenticationManager authenticationManager; // para verificar credenciales

    @Override
    public AuthResponse register(AuthRequest request) {
        // Verificamos que no exista otro usuario con el mismo username
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Creamos el objeto Usuario y le seteamos los datos
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setNombre(request.getNombre());
        // encode() encripta la contraseña, nunca guardamos texto plano en la BD
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.USER); // CAMBIAR ANTES DE CREA USUARIO O ADMIN

        // Guardamos el usuario en la BD
        usuarioRepository.save(usuario);

        // Generamos el token con los datos del usuario y lo devolvemos
        String token = jwtService.generateToken(usuario);
        return AuthResponse.builder().token(token).build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        // authenticationManager verifica que el username y password sean correctos
        // si son incorrectos lanza una excepción automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), // username que mando el cliente
                        request.getPassword()  // password que mando el cliente
                )
        );

        // Si llegamos acá es porque las credenciales son correctas
        // Buscamos el usuario en la BD para generar el token
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no econtrado"));

        // Generamos el token y lo devolvemos
        String token = jwtService.generateToken(usuario);
        return AuthResponse.builder().token(token).build();
    }

}



