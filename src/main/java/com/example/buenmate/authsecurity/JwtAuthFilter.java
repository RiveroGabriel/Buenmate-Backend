package com.example.buenmate.authsecurity;

import com.example.buenmate.authsecurity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

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
        System.out.println("HEADER RECIBIDO: " + autoHeader);

        // 2. Si no hay token o no empieza con "Bearer ", dejamos pasar sin autenticar
        if (autoHeader == null || !autoHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Sacamos el token del header (cortamos el "Bearer " del principio)
        String token = autoHeader.substring(7);


        // 4. Extraemos el username que está guardado adentro del token
        String username = jwtService.extractUsername(token);

        // 5. Si hay username y el usuario no está autenticado todavía en esta request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) ;

        // 6. Buscamos el usuario en la BD
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // 7. Verificamos que el token sea válido y no haya vencido
        if (jwtService.isTokenValid(token, user)) {

            // 8. Autenticamos al usuario en Spring Security para esta request
            var auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        }


        // 9. Dejamos seguir el request hacia el controller
        filterChain.doFilter(request,response);





    }

}
