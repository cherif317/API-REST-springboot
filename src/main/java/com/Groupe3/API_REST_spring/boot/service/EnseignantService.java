package com.Groupe3.API_REST_spring.boot.service;

import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.entity.Enseignant;
import com.Groupe3.API_REST_spring.boot.exception.BadRequestException;
import com.Groupe3.API_REST_spring.boot.exception.ResourceNotFoundException;
import com.Groupe3.API_REST_spring.boot.repository.EnseignantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;

    public Page<EnseignantDTO> findAll(Pageable pageable) {
        return enseignantRepository.findAll(pageable).map(this::toDTO);
    }

    public EnseignantDTO findById(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'id : " + id));
        return toDTO(enseignant);
    }

    public EnseignantDTO create(EnseignantCreateDTO dto) {
        if (enseignantRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email déjà utilisé par un autre enseignant");
        }
        Enseignant enseignant = Enseignant.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .specialite(dto.getSpecialite())
                .telephone(dto.getTelephone())
                .build();
        return toDTO(enseignantRepository.save(enseignant));
    }

    public EnseignantDTO update(Long id, EnseignantUpdateDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'id : " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(enseignant.getEmail())) {
            if (enseignantRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new BadRequestException("Email déjà utilisé par un autre enseignant");
            }
        }

        if (dto.getNom() != null) enseignant.setNom(dto.getNom());
        if (dto.getPrenom() != null) enseignant.setPrenom(dto.getPrenom());
        if (dto.getEmail() != null) enseignant.setEmail(dto.getEmail());
        if (dto.getSpecialite() != null) enseignant.setSpecialite(dto.getSpecialite());
        if (dto.getTelephone() != null) enseignant.setTelephone(dto.getTelephone());

        return toDTO(enseignantRepository.save(enseignant));
    }

    public void delete(Long id) {
        if (!enseignantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enseignant non trouvé avec l'id : " + id);
        }
        enseignantRepository.deleteById(id);
    }

    public Page<EnseignantDTO> findByNom(String nom, Pageable pageable) {
        return enseignantRepository.findByNomContainingIgnoreCase(nom, pageable).map(this::toDTO);
    }

    private EnseignantDTO toDTO(Enseignant e) {
        return EnseignantDTO.builder()
                .id(e.getId())
                .nom(e.getNom())
                .prenom(e.getPrenom())
                .email(e.getEmail())
                .specialite(e.getSpecialite())
                .telephone(e.getTelephone())
                .build();
    }
}
