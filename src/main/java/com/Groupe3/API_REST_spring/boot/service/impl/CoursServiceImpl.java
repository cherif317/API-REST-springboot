package com.Groupe3.API_REST_spring.boot.service.impl;

import com.Groupe3.API_REST_spring.boot.dto.cours.CoursDTO;
import com.Groupe3.API_REST_spring.boot.entity.Cours;
import com.Groupe3.API_REST_spring.boot.entity.Enseignant;
import com.Groupe3.API_REST_spring.boot.exception.ResourceNotFoundException;
import com.Groupe3.API_REST_spring.boot.mapper.CoursMapper;
import com.Groupe3.API_REST_spring.boot.repository.CoursRepository;
import com.Groupe3.API_REST_spring.boot.repository.EnseignantRepository;
import com.Groupe3.API_REST_spring.boot.service.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CoursServiceImpl implements CoursService {

    private final CoursRepository coursRepository;
    private final EnseignantRepository enseignantRepository;
    private final CoursMapper coursMapper;

    @Override
    public Page<CoursDTO> findAll(Pageable pageable) {
        return coursRepository.findAll(pageable).map(coursMapper::toDTO);
    }

    @Override
    public Page<CoursDTO> findByNom(String nom, Pageable pageable) {
        return coursRepository.findByNomContainingIgnoreCase(nom, pageable).map(coursMapper::toDTO);
    }

    @Override
    public CoursDTO findById(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        return coursMapper.toDTO(cours);
    }

    @Override
    public CoursDTO create(CoursDTO dto) {
        Cours cours = coursMapper.toEntity(dto);
        
        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = enseignantRepository.findById(dto.getEnseignantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'ID: " + dto.getEnseignantId()));
            cours.setEnseignant(enseignant);
        }

        Cours savedCours = coursRepository.save(cours);
        return coursMapper.toDTO(savedCours);
    }

    @Override
    public CoursDTO update(Long id, CoursDTO dto) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));

        cours.setNom(dto.getNom());
        cours.setDescription(dto.getDescription());
        cours.setCredits(dto.getCredits());

        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = enseignantRepository.findById(dto.getEnseignantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'ID: " + dto.getEnseignantId()));
            cours.setEnseignant(enseignant);
        }

        Cours updatedCours = coursRepository.save(cours);
        return coursMapper.toDTO(updatedCours);
    }

    @Override
    public void delete(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        coursRepository.delete(cours);
    }
}
