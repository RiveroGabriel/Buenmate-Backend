package com.example.buenmate.Service.Impl;

import com.example.buenmate.Enums.Rol;
import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(Rol.USER); // asigno el rol, no lo leo
        return usuarioRepository.save(usuario);
    }

}
