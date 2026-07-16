package com.Groupe3.API_REST_spring.boot.mapper;

import com.Groupe3.API_REST_spring.boot.dto.cours.CoursDTO;
import com.Groupe3.API_REST_spring.boot.entity.Cours;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoursMapper {

    @Mapping(source = "enseignant.id", target = "enseignantId")
    @Mapping(source = "enseignant.nom", target = "enseignantNom")
    CoursDTO toDTO(Cours cours);

    @Mapping(target = "enseignant", ignore = true)
    @Mapping(target = "inscriptions", ignore = true)
    Cours toEntity(CoursDTO dto);

}