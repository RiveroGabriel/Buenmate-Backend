package com.example.buenmate.authsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
// DTO que recibe los datos del login/registro
public class AuthRequest {

    @NonNull
    @NotBlank
    private String nombre;

    @NonNull
    @NotBlank
    private String email; // buscamos

    @NonNull
    @NotBlank
    private String password;
}