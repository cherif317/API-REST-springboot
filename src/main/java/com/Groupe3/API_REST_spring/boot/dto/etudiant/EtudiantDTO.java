package com.Groupe3.API_REST_spring.boot.dto.etudiant;

import lombok.Data;

@Data
public class EtudiantDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String matricule;

    private String adresse;

    private String photo;

}