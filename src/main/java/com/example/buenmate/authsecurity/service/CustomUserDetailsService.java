package com.example.buenmate.authsecurity.service;

import com.example.buenmate.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// Spring Security usa esta clase para buscar el usuario en la BD cuando necesita autenticar
// Implementa UserDetailsService, que es la interfaz que Spring Security espera
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository; // para buscar el usuario en la BD

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        // Spring Security llama a este método automáticamente cuando necesita verificar un usuario por su email
        // Busca el usuario en la BD por username, si no existe lanza excepción
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado "));
    }

}