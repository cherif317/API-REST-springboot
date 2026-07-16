package com.Groupe3.API_REST_spring.boot.service;

import com.Groupe3.API_REST_spring.boot.dto.cours.CoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoursService {
    Page<CoursDTO> findAll(Pageable pageable);
    Page<CoursDTO> findByNom(String nom, Pageable pageable);
    CoursDTO findById(Long id);
    CoursDTO create(CoursDTO dto);
    CoursDTO update(Long id, CoursDTO dto);
    void delete(Long id);
}
