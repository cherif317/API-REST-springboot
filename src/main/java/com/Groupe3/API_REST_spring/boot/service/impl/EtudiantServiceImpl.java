package com.Groupe3.API_REST_spring.boot.service.impl;

import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantCreateDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantDTO;
import com.Groupe3.API_REST_spring.boot.dto.etudiant.EtudiantUpdateDTO;
import com.Groupe3.API_REST_spring.boot.entity.Etudiant;
import com.Groupe3.API_REST_spring.boot.entity.Role;
import com.Groupe3.API_REST_spring.boot.enums.RoleName;
import com.Groupe3.API_REST_spring.boot.exception.ResourceNotFoundException;
import com.Groupe3.API_REST_spring.boot.mapper.EtudiantMapper;
import com.Groupe3.API_REST_spring.boot.repository.EtudiantRepository;
import com.Groupe3.API_REST_spring.boot.repository.RoleRepository;
import com.Groupe3.API_REST_spring.boot.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final RoleRepository roleRepository;
    private final EtudiantMapper etudiantMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<EtudiantDTO> findAll(Pageable pageable) {
        return etudiantRepository.findAll(pageable).map(etudiantMapper::toDTO);
    }

    @Override
    public Page<EtudiantDTO> findByNom(String nom, Pageable pageable) {
        return etudiantRepository.findByNomContainingIgnoreCase(nom, pageable).map(etudiantMapper::toDTO);
    }

    @Override
    public EtudiantDTO findById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + id));
        return etudiantMapper.toDTO(etudiant);
    }

    @Override
    public EtudiantDTO create(EtudiantCreateDTO dto) {
        if (etudiantRepository.existsByMatricule(dto.getMatricule())) {
            throw new RuntimeException("Matricule déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        Role etudiantRole = roleRepository.findByNom(RoleName.ETUDIANT)
                .orElseGet(() -> roleRepository.save(Role.builder().nom(RoleName.ETUDIANT).build()));
        roles.add(etudiantRole);

        Etudiant etudiant = etudiantMapper.toEntity(dto);
        etudiant.setRoles(roles);
        etudiant.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDTO(savedEtudiant);
    }

    @Override
    public EtudiantDTO update(Long id, EtudiantUpdateDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + id));

        etudiantMapper.updateEntityFromDTO(dto, etudiant);
        
        if (dto.getMatricule() != null && !dto.getMatricule().equals(etudiant.getMatricule())) {
            if (etudiantRepository.existsByMatricule(dto.getMatricule())) {
                throw new RuntimeException("Matricule déjà utilisé");
            }
            etudiant.setMatricule(dto.getMatricule());
        }

        Etudiant updatedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDTO(updatedEtudiant);
    }

    @Override
    public void delete(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + id));
        etudiantRepository.delete(etudiant);
    }

    @Override
    public String uploadPhoto(Long id, String fileName) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + id));
        etudiant.setPhoto(fileName);
        etudiantRepository.save(etudiant);
        return fileName;
    }

    @Override
    public String uploadDocument(Long id, String fileName) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouvé avec l'ID: " + id));
        etudiantRepository.save(etudiant);
        return fileName;
    }
}
