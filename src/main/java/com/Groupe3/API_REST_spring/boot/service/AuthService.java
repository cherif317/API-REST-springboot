package com.Groupe3.API_REST_spring.boot.service;

import com.Groupe3.API_REST_spring.boot.dto.auth.AuthResponse;
import com.Groupe3.API_REST_spring.boot.dto.auth.LoginRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RegisterRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RefreshTokenRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
