package com.Groupe3.API_REST_spring.boot.mapper;

import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.entity.Enseignant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        builder = @org.mapstruct.Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EnseignantMapper {

    EnseignantDTO toDTO(Enseignant enseignant);

    Enseignant toEntity(EnseignantCreateDTO dto);

    void updateEntityFromDTO(EnseignantUpdateDTO dto, @MappingTarget Enseignant entity);
}