package com.example.buenmate.authsecurity.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {

    // Clave secreta para firmar el token, la definimos en application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Tiempo de vida del token en milisegundos, también en application.properties
    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generateToken(UserDetails user){
    String rol = user.getAuthorities().stream()
            .findFirst() // encuentra el primero
            .map(GrantedAuthority::getAuthority) // obtiene el nombre del rol
            .orElse("USER");

    if (rol.startsWith("ROLE_")){
        rol = rol.substring(5); // elimina "ROLE_"
    }

        return Jwts.builder()
                .setSubject(user.getUsername()) // el subject es el username
                .claim("rol", rol) // agregamos el rol como claim
                .setIssuedAt(new Date()) // fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // fecha de vencimiento
                .signWith(getSigningKey()) // firma con la clave secreta
                .compact(); // genera el token en formato String
    }


    //Convierte la clave secrea en un objeto que puede usar el algoritmo HMAC
    private SecretKey getSigningKey() {
        byte [] keyBytes = Decoders.BASE64.decode(secretKey); // decodifica la clave secreta de Base64
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrae el username que está guardado dentro del token
    public String extractUsername(String token){
        // Claims::getSubject le dice "quiero el subject del token"
        // el subject es el username que guardamos cuando generamos el token
        return extractClaim(token, Claims::getSubject);
    }

    // Verifica que el token sea válido y pertenezca al usuario correcto
    public boolean isTokenValid(String token, UserDetails user){
        String username = extractUsername(token);// saca el username del token
        // compara que el username del token sea el mismo que el del usuario
        // Y que el token no haya vencido
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    // Verifica si el token ya venció
    private boolean isTokenExpired(String token){
        // getExpiration saca la fecha de vencimiento del token
        // before(new Date()) pregunta: ¿esa fecha es anterior a ahora?
        // si venció → true, si sigue vigente → false
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Método genérico para extraer cualquier dato del token
    // <T> significa que puede devolver cualquier tipo (String, Date, etc)
    // resolver es la función que le decís qué querés extraer
    private <T> T extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // verifica la firma con la clave secreta
                .build()
                .parseSignedClaims(token)   // desencripta y lee el token
                .getPayload();              // obtiene los datos del token
        return resolver.apply(claims);      // aplica la función y devuelve el dato pedido
    }




}
