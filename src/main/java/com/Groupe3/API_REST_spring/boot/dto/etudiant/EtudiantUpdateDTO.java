package com.Groupe3.API_REST_spring.boot.dto.etudiant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EtudiantUpdateDTO {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Email
    private String email;

    private String telephone;

    private String matricule;

    private LocalDate dateNaissance;

    private String adresse;

}
