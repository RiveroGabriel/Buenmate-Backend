package com.example.buenmate.Repository;

import com.example.buenmate.Model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmail(String email); // busca emial en la bd
}
