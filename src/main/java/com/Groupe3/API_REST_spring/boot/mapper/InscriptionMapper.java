package com.Groupe3.API_REST_spring.boot.mapper;

import com.Groupe3.API_REST_spring.boot.dto.inscription.InscriptionDTO;
import com.Groupe3.API_REST_spring.boot.entity.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InscriptionMapper {

    @Mapping(source = "etudiant.id", target = "etudiantId")
    @Mapping(source = "cours.id", target = "coursId")
    @Mapping(source = "etudiant.nom", target = "etudiantNom")
    @Mapping(source = "cours.nom", target = "coursNom")
    InscriptionDTO toDTO(Inscription inscription);

    @Mapping(target = "etudiant", ignore = true)
    @Mapping(target = "cours", ignore = true)
    Inscription toEntity(InscriptionDTO dto);

}