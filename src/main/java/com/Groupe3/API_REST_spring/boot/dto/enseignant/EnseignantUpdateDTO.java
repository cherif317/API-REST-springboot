package com.Groupe3.API_REST_spring.boot.dto.enseignant;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnseignantUpdateDTO {
    private String nom;
    private String prenom;
    private String email;
    private String specialite;
    private String telephone;
}