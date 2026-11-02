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
            import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
            @EnableMethodSecurity(prePostEnabled = true)
            @RequiredArgsConstructor
            // Configurar toda la seguridad de la aplicacion
            public class SecurityConfig {

                private final JwtAuthFilter jwtAuthFilter;
                private final CustomUserDetailsService userDetailsService;

                @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                            .csrf(csrf -> csrf.disable()) // deshabilitamos CSRF porque usamos tokens, no sesiones
                            .cors(cors -> cors.configurationSource(corsConfig()))
                            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // sin sesiones, cada request se autentica con el token
                            .authorizeHttpRequests(auth -> auth
                                    // Solo endpoints públicos
                                            // Solo registro y login son públicos porque todavía no hay usuario autenticado.
                                            .requestMatchers(HttpMethod.POST,
                                                    "/api/v1/auth/register",
                                                    "/api/v1/auth/login"
                                            ).permitAll()

                                       // La documentación de la API también queda pública.
                                            .requestMatchers(
                                                    "/swagger-ui/**",
                                                    "/swagger-ui.html",
                                                    "/v3/api-docs/**"
                                            ).permitAll()



                                    .requestMatchers(HttpMethod.GET,
                            "/api/productos/activos",
                                     "/api/datos-transferencia",
                                     "/uploads/**"
                                    ).permitAll()


                                    // Solo usuarios con rol ADMIN pueden acceder a estos endpoints
                                    .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT, "/api/admin/datos-transferencia").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/admin/pedidos").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PATCH, "/api/admin/pedidos/**", "/api/productos/**").hasRole("ADMIN")

                                    .anyRequest().authenticated()
                            )
                            .authenticationProvider(authenticationProvider())
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
                    config.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));

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
