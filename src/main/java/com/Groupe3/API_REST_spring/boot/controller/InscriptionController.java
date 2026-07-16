package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.dto.inscription.InscriptionDTO;
import com.Groupe3.API_REST_spring.boot.service.InscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Inscriptions", description = "API pour la gestion des inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Liste de toutes les inscriptions", description = "Récupère la liste de toutes les inscriptions")
    public ResponseEntity<List<InscriptionDTO>> findAll() {
        return ResponseEntity.ok(inscriptionService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer une inscription par ID", description = "Récupère les détails d'une inscription spécifique")
    public ResponseEntity<InscriptionDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.findById(id));
    }

    @GetMapping("/etudiant/{etudiantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT', 'ETUDIANT')")
    @Operation(summary = "Inscriptions d'un étudiant", description = "Récupère toutes les inscriptions d'un étudiant spécifique")
    public ResponseEntity<List<InscriptionDTO>> findByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(inscriptionService.findByEtudiant(etudiantId));
    }

    @GetMapping("/cours/{coursId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Inscriptions à un cours", description = "Récupère toutes les inscriptions pour un cours spécifique")
    public ResponseEntity<List<InscriptionDTO>> findByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(inscriptionService.findByCours(coursId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Inscrire un étudiant à un cours", description = "Crée une nouvelle inscription d'un étudiant à un cours")
    public ResponseEntity<InscriptionDTO> create(@RequestParam Long etudiantId, @RequestParam Long coursId) {
        InscriptionDTO created = inscriptionService.create(etudiantId, coursId);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une inscription", description = "Supprime une inscription de la base de données")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
