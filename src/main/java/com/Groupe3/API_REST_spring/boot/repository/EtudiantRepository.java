package com.Groupe3.API_REST_spring.boot.repository;

import com.Groupe3.API_REST_spring.boot.entity.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Page<Etudiant> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    boolean existsByMatricule(String matricule);

}