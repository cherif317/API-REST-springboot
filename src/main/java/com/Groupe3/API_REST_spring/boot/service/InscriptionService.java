package com.Groupe3.API_REST_spring.boot.service;

import com.Groupe3.API_REST_spring.boot.dto.inscription.InscriptionDTO;

import java.util.List;

public interface InscriptionService {
    List<InscriptionDTO> findAll();
    InscriptionDTO findById(Long id);
    List<InscriptionDTO> findByEtudiant(Long etudiantId);
    List<InscriptionDTO> findByCours(Long coursId);
    InscriptionDTO create(Long etudiantId, Long coursId);
    InscriptionDTO update(Long id, Long etudiantId, Long coursId);
    void delete(Long id);
}
