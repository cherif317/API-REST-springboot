package com.Groupe3.API_REST_spring.boot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ETUDIANT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant extends Utilisateur {


    @NotBlank
    @Column(unique = true)
    private String matricule;


    @Past
    private LocalDate dateNaissance;


    private String adresse;


    /**
     * Chemin de la photo
     * Exemple :
     * uploads/photos/diallo.jpg
     */
    private String photo;


    @OneToMany(
            mappedBy = "etudiant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Inscription> inscriptions = new ArrayList<>();
}