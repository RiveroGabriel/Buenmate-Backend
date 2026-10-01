package com.example.buenmate.authsecurity;

import com.example.buenmate.authsecurity.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
// Intercepta CADA request antes de que llegue al controller
// Su trabajo: verificar si el token es válido y autenticar al usuario
public class JwtAuthFilter extends OncePerRequestFilter {


    // Necesitamos JwtService para validar el token
    private final JwtService jwtService;
    // Necesitamos UserDetailsService para buscar el usuario en la BD
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Busca el header "Authorization" en el request
        String autoHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2. Si no hay token o no empieza con "Bearer ", dejamos pasar sin autenticar
        if (autoHeader == null || !autoHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = autoHeader.substring(7);   // 3. Sacamos el token del header (cortamos el "Bearer " del principio)

        try {
            String username = jwtService.extractUsername(token);         // 4. Extraemos el username que está guardado adentro del token

            // 5. Si hay username y el usuario no está autenticado todavía en esta request
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails user = userDetailsService.loadUserByUsername(username); // 6. Buscamos el usuario en la BD

                if (jwtService.isTokenValid(token, user)) {    // 7. Verificamos que el token sea válido y no haya vencido

                    var auth = new UsernamePasswordAuthenticationToken(  // 8. Autenticamos al usuario en Spring Security para esta request
                            user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido, mal formado o expirado -> 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"error\":\"Token invalido o expirado\"}");
            return;
        } catch (UsernameNotFoundException e) {
            // El usuario del token ya no existe -> 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
            return;
        }

        filterChain.doFilter(request,response);   // 9. Dejamos seguir el request hacia el controller





    }

}
