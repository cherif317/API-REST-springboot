package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.service.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Étudiants", description = "API pour la gestion des étudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @Operation(summary = "Liste paginée des étudiants", description = "Récupère la liste des étudiants avec pagination")
    public ResponseEntity<Page<EtudiantDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(etudiantService.findAll(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @Operation(summary = "Rechercher des étudiants par nom", description = "Recherche des étudiants dont le nom contient la chaîne spécifiée")
    public ResponseEntity<Page<EtudiantDTO>> findByNom(
            @RequestParam String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return ResponseEntity.ok(etudiantService.findByNom(nom, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT', 'ETUDIANT')")
    @Operation(summary = "Récupérer un étudiant par ID", description = "Récupère les détails d'un étudiant spécifique")
    public ResponseEntity<EtudiantDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un étudiant", description = "Crée un nouvel étudiant")
    public ResponseEntity<EtudiantDTO> create(@Valid @RequestBody EtudiantCreateDTO dto) {
        EtudiantDTO created = etudiantService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un étudiant", description = "Met à jour les informations d'un étudiant")
    public ResponseEntity<EtudiantDTO> update(@PathVariable Long id, @Valid @RequestBody EtudiantUpdateDTO dto) {
        EtudiantDTO updated = etudiantService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un étudiant", description = "Supprime un étudiant de la base de données")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etudiantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
