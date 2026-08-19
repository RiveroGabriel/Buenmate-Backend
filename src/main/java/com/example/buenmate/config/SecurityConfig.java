package com.example.buenmate.config;


import com.example.buenmate.authsecurity.JwtAuthFilter;
import com.example.buenmate.authsecurity.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// Configurar toda la seguridad de la aplicacion
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // deshabilitamos CSRF porque usamos tokens, no sesiones
                .csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfig()))
                // sin sesiones, cada request se autentica con el token
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // estas rutas son públicas, no necesitan token
                        .requestMatchers("/api/v1/auth/**").permitAll() //cualquier otra ruta necesita token válido
                        .requestMatchers(HttpMethod.POST,"/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/productos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/categorias/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                // agregamos nuestro filtro JWT antes del filtro de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Spring usa esto para verificar credenciales en el login
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // le decimos a Spring cómo buscar usuarios y cómo verificar contraseñas
        DaoAuthenticationProvider provider =  new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCrypt es el algoritmo que usamos para encriptar contraseñas
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfig() {
        // creo la configuracion CORS
        CorsConfiguration config = new CorsConfiguration();

        // solo el frontend puede hacer request al backend
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // permitimos cualquier header, incluyendo Authorization con el token
        config.setAllowedHeaders(List.of("*"));

        // permitimos enviar credenciales (el token)
        config.setAllowCredentials(true);

        // aplicamos esta configuración a todas las rutas del backend
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
