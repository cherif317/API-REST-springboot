package com.Groupe3.API_REST_spring.boot.entity;

import com.Groupe3.API_REST_spring.boot.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName nom;

    @ManyToMany(mappedBy = "roles")
    private List<Utilisateur> utilisateurs = new ArrayList<>();
}