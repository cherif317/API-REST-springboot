package com.Groupe3.API_REST_spring.boot.repository;

import com.Groupe3.API_REST_spring.boot.entity.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Page<Enseignant> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}