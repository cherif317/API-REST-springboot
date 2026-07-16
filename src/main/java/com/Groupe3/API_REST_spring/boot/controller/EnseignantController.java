package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantDTO;
import com.Groupe3.API_REST_spring.boot.dto.enseignant.EnseignantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.service.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/enseignants")
@Tag(name = "Enseignants", description = "API pour la gestion des enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<Page<EnseignantDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(enseignantService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<EnseignantDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(enseignantService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnseignantDTO> create(@Valid @RequestBody EnseignantCreateDTO dto) {
        return new ResponseEntity<>(enseignantService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnseignantDTO> update(@PathVariable Long id, @Valid @RequestBody EnseignantUpdateDTO dto) {
        return ResponseEntity.ok(enseignantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enseignantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<Page<EnseignantDTO>> findByNom(@RequestParam String nom, Pageable pageable) {
        return ResponseEntity.ok(enseignantService.findByNom(nom, pageable));
    }
}