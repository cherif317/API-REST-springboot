package com.Groupe3.API_REST_spring.boot.controller;


import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.service.EtudiantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Étudiants",
        description = "API pour la gestion des étudiants"
)
public class EtudiantController {


    private final EtudiantService etudiantService;



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENSEIGNANT')")
    public ResponseEntity<Page<EtudiantDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){

        Sort sort =
                sortDir.equalsIgnoreCase("desc")
                        ?
                        Sort.by(sortBy).descending()
                        :
                        Sort.by(sortBy).ascending();


        Pageable pageable =
                PageRequest.of(page,size,sort);


        return ResponseEntity.ok(
                etudiantService.findAll(pageable)
        );
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENSEIGNANT','ETUDIANT')")
    public ResponseEntity<EtudiantDTO> findById(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                etudiantService.findById(id)
        );
    }



    /**
     * Afficher la photo d'un étudiant
     */
    @GetMapping("/{id}/photo")
    @PreAuthorize("hasAnyRole('ADMIN','ENSEIGNANT','ETUDIANT')")
    @Operation(
            summary="Afficher la photo d'un étudiant"
    )
    public ResponseEntity<Resource> getPhoto(
            @PathVariable Long id
    ){


        EtudiantDTO etudiant =
                etudiantService.findById(id);



        if(etudiant.getPhoto()==null ||
                etudiant.getPhoto().isEmpty()){

            return ResponseEntity.notFound()
                    .build();
        }



        Path path =
                Paths.get(etudiant.getPhoto());



        if(!Files.exists(path)){

            return ResponseEntity.notFound()
                    .build();

        }



        Resource resource =
                new FileSystemResource(path);



        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }



    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantDTO> create(
            @Valid @RequestBody EtudiantCreateDTO dto
    ){

        return ResponseEntity.ok(
                etudiantService.create(dto)
        );
    }




    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantUpdateDTO dto
    ){

        return ResponseEntity.ok(
                etudiantService.update(id,dto)
        );
    }





    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){

        etudiantService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }

}