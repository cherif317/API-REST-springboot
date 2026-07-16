package com.Groupe3.API_REST_spring.boot.mapper;

import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.entity.Etudiant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EtudiantMapper {

    EtudiantDTO toDTO(Etudiant etudiant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "inscriptions", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "compteActive", constant = "true")
    @Mapping(target = "compteVerifie", constant = "false")
    Etudiant toEntity(EtudiantCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "inscriptions", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    void updateEntityFromDTO(EtudiantUpdateDTO dto, @MappingTarget Etudiant entity);

}