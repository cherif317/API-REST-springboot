package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.dto.auth.AuthResponse;
import com.Groupe3.API_REST_spring.boot.dto.auth.LoginRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RegisterRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RefreshTokenRequest;
import com.Groupe3.API_REST_spring.boot.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }
}
