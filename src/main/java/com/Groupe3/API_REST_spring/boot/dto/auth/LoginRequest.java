package com.Groupe3.API_REST_spring.boot.dto.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "L'email est obligatoire")
    @JsonAlias({"username", "email"})
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @JsonAlias({"password", "motDePasse"})
    private String motDePasse;

}