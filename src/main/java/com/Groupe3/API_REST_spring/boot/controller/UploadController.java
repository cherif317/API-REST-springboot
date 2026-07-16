package com.Groupe3.API_REST_spring.boot.controller;

import com.Groupe3.API_REST_spring.boot.service.EtudiantService;
import com.Groupe3.API_REST_spring.boot.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Upload", description = "API pour l'upload de fichiers")
public class UploadController {

    private final UploadService uploadService;
    private final EtudiantService etudiantService;

    @PostMapping("/etudiants/{id}/photo")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    @Operation(summary = "Upload photo de profil", description = "Upload la photo de profil d'un étudiant (max 2Mo)")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws Exception {
        
        String fileName = uploadService.uploadPhoto(file, id);
        etudiantService.uploadPhoto(id, fileName);
        
        return ResponseEntity.ok(fileName);
    }

    @PostMapping("/etudiants/{id}/docs")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    @Operation(summary = "Upload document PDF", description = "Upload un document PDF pour un étudiant (max 2Mo)")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws Exception {
        
        String fileName = uploadService.uploadDocument(file, id);
        etudiantService.uploadDocument(id, fileName);
        
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/photos/{fileName}")
    @Operation(summary = "Récupérer une photo", description = "Récupère une photo de profil par son nom de fichier")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String fileName) {
        try {
            byte[] imageBytes = uploadService.getPhoto(fileName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/documents/{fileName}")
    @Operation(summary = "Récupérer un document", description = "Récupère un document PDF par son nom de fichier")
    public ResponseEntity<byte[]> getDocument(@PathVariable String fileName) {
        try {
            byte[] documentBytes = uploadService.getDocument(fileName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(documentBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
