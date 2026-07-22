package com.Groupe3.API_REST_spring.boot.repository;

import com.Groupe3.API_REST_spring.boot.entity.Utilisateur;
import com.Groupe3.API_REST_spring.boot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(Utilisateur user);

    void deleteByUser(Utilisateur user);
}
