package com.example.buenmate.Model;


import com.example.buenmate.Enums.Rol;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;


    // --- Métodos que pide la interfaz UserDetails (Spring Security los necesita) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // le dice a Spring qué "rol" tiene este usuario
        return List.of(new SimpleGrantedAuthority("ROLE_"+ rol.name()));
    }

    @Override
    public String getUsername() {
        // Spring Security espera un "username" - nosotros usamos el email como tal
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
