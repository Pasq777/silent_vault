package com.silentvault.auth_service.config;

import com.silentvault.auth_service.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/h2-console/**",
                                "/swagger-ui/**",           // Swagger UI
                                "/swagger-ui.html",         // Swagger UI (versione vecchia)
                                "/v3/api-docs/**",          // OpenAPI JSON
                                "/v3/api-docs",             // OpenAPI entrypoint
                                "/webjars/**"               // risorse statiche Swagger
                        ).permitAll()
                        .anyRequest()
                        .authenticated() //tolgo autenticazione da login, registrazione e console h2
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) //per evitare l'errore "Refused to display in a frame" quando apro http://localhost:8080/h2-console, disattivo l'header così:
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //aggiungo filtro custom jwt

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}