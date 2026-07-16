package com.Groupe3.API_REST_spring.boot.repository;

import com.Groupe3.API_REST_spring.boot.entity.Cours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursRepository extends JpaRepository<Cours, Long> {

    Page<Cours> findByNomContainingIgnoreCase(String nom, Pageable pageable);

}