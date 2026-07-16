package com.Groupe3.API_REST_spring.boot.repository;

import com.Groupe3.API_REST_spring.boot.entity.Role;
import com.Groupe3.API_REST_spring.boot.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNom(RoleName nom);

    boolean existsByNom(RoleName nom);

}