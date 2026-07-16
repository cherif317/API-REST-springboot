package com.Groupe3.API_REST_spring.boot.service;

import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EtudiantService {
    Page<EtudiantDTO> findAll(Pageable pageable);
    Page<EtudiantDTO> findByNom(String nom, Pageable pageable);
    EtudiantDTO findById(Long id);
    EtudiantDTO create(EtudiantCreateDTO dto);
    EtudiantDTO update(Long id, EtudiantUpdateDTO dto);
    void delete(Long id);
    String uploadPhoto(Long id, String fileName);
    String uploadDocument(Long id, String fileName);
}
