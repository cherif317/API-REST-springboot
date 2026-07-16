package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.dto.cours.CoursDTO;
import com.Groupe3.API_REST_spring.boot.service.CoursService;
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
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Cours", description = "API pour la gestion des cours")
public class CoursController {

    private final CoursService coursService;

    @GetMapping
    @Operation(summary = "Liste des cours", description = "Récupère la liste de tous les cours disponibles")
    public ResponseEntity<Page<CoursDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(coursService.findAll(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des cours par nom", description = "Recherche des cours dont le nom contient la chaîne spécifiée")
    public ResponseEntity<Page<CoursDTO>> findByNom(
            @RequestParam String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return ResponseEntity.ok(coursService.findByNom(nom, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un cours par ID", description = "Récupère les détails d'un cours spécifique")
    public ResponseEntity<CoursDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un cours", description = "Crée un nouveau cours")
    public ResponseEntity<CoursDTO> create(@Valid @RequestBody CoursDTO dto) {
        CoursDTO created = coursService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un cours", description = "Met à jour les informations d'un cours")
    public ResponseEntity<CoursDTO> update(@PathVariable Long id, @Valid @RequestBody CoursDTO dto) {
        CoursDTO updated = coursService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un cours", description = "Supprime un cours de la base de données")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
