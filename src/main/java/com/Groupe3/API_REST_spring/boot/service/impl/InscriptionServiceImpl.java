package com.Groupe3.API_REST_spring.boot.service.impl;

import com.Groupe3.API_REST_spring.boot.dto.inscription.InscriptionDTO;
import com.Groupe3.API_REST_spring.boot.entity.Cours;
import com.Groupe3.API_REST_spring.boot.entity.Etudiant;
import com.Groupe3.API_REST_spring.boot.entity.Inscription;
import com.Groupe3.API_REST_spring.boot.exception.ResourceNotFoundException;
import com.Groupe3.API_REST_spring.boot.mapper.InscriptionMapper;
import com.Groupe3.API_REST_spring.boot.repository.CoursRepository;
import com.Groupe3.API_REST_spring.boot.repository.EtudiantRepository;
import com.Groupe3.API_REST_spring.boot.repository.InscriptionRepository;
import com.Groupe3.API_REST_spring.boot.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final InscriptionMapper inscriptionMapper;

    @Override
    public List<InscriptionDTO> findAll() {
        return inscriptionRepository.findAll().stream()
                .map(inscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InscriptionDTO findById(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec l'ID: " + id));
        return inscriptionMapper.toDTO(inscription);
    }

    @Override
    public List<InscriptionDTO> findByEtudiant(Long etudiantId) {
        return inscriptionRepository.findByEtudiantId(etudiantId).stream()
                .map(inscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InscriptionDTO> findByCours(Long coursId) {
        return inscriptionRepository.findByCoursId(coursId).stream()
                .map(inscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InscriptionDTO create(Long etudiantId, Long coursId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + etudiantId));

        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + coursId));

        if (inscriptionRepository.existsByEtudiantIdAndCoursId(etudiantId, coursId)) {
            throw new RuntimeException("L'étudiant est déjà inscrit à ce cours");
        }

        Inscription inscription = Inscription.builder()
                .etudiant(etudiant)
                .cours(cours)
                .build();

        Inscription savedInscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDTO(savedInscription);
    }

    @Override
    public InscriptionDTO update(Long id, Long etudiantId, Long coursId) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec l'ID: " + id));

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + etudiantId));

        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + coursId));

        boolean dejaInscritAilleurs = inscriptionRepository.existsByEtudiantIdAndCoursId(etudiantId, coursId)
                && !(inscription.getEtudiant().getId().equals(etudiantId)
                && inscription.getCours().getId().equals(coursId));

        if (dejaInscritAilleurs) {
            throw new RuntimeException("L'étudiant est déjà inscrit à ce cours");
        }

        inscription.setEtudiant(etudiant);
        inscription.setCours(cours);

        Inscription updatedInscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDTO(updatedInscription);
    }

    @Override
    public void delete(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec l'ID: " + id));
        inscriptionRepository.delete(inscription);
    }
}