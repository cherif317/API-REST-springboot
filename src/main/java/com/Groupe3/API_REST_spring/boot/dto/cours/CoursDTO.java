package com.Groupe3.API_REST_spring.boot.dto.cours;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursDTO {

    private Long id;

    private String nom;

    private String description;

    private Integer credits;

    private Long enseignantId;

    private String enseignantNom;

}
