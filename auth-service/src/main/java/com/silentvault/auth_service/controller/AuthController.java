package com.silentvault.auth_service.controller;

import com.silentvault.auth_service.dto.LoginRequest;
import com.silentvault.auth_service.dto.RegisterRequest;
import com.silentvault.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registra un nuovo utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registrazione avvenuta con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }




    @Operation(summary = "Login utente e generazione token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login riuscito"),
            @ApiResponse(responseCode = "401", description = "Credenziali non valide")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }



    @Operation(
            summary = "Restituisce l'utente attualmente autenticato"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente autenticato"),
            @ApiResponse(responseCode = "401", description = "Non autenticato")
    })
    @GetMapping("/whoami")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
