package com.Groupe3.API_REST_spring.boot.service.impl;

import com.Groupe3.API_REST_spring.boot.service.JwtService;
import com.Groupe3.API_REST_spring.boot.dto.auth.AuthResponse;
import com.Groupe3.API_REST_spring.boot.dto.auth.LoginRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RegisterRequest;
import com.Groupe3.API_REST_spring.boot.dto.auth.RefreshTokenRequest;
import com.Groupe3.API_REST_spring.boot.entity.Role;
import com.Groupe3.API_REST_spring.boot.entity.Utilisateur;
import com.Groupe3.API_REST_spring.boot.entity.RefreshToken;
import com.Groupe3.API_REST_spring.boot.enums.RoleName;
import com.Groupe3.API_REST_spring.boot.repository.RoleRepository;
import com.Groupe3.API_REST_spring.boot.repository.UtilisateurRepository;
import com.Groupe3.API_REST_spring.boot.repository.RefreshTokenRepository;
import com.Groupe3.API_REST_spring.boot.service.AuthService;
import com.Groupe3.API_REST_spring.boot.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        String roleStr = request.getRole();
        if (roleStr != null && !roleStr.trim().isEmpty()) {
            try {
                RoleName roleName = RoleName.valueOf(roleStr.trim().toUpperCase());
                Role role = roleRepository.findByNom(roleName)
                        .orElseGet(() -> roleRepository.save(Role.builder().nom(roleName).build()));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Rôle invalide. Les rôles autorisés sont : ADMIN, ENSEIGNANT, ETUDIANT");
            }
        } else {
            Role defaultRole = roleRepository.findByNom(RoleName.ETUDIANT)
                    .orElseGet(() -> roleRepository.save(Role.builder().nom(RoleName.ETUDIANT).build()));
            roles.add(defaultRole);
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .roles(roles)
                .compteActive(true)
                .compteVerifie(false)
                .build();

        utilisateurRepository.save(utilisateur);

        String token = jwtService.generateToken(utilisateur);
        RefreshToken refreshToken = createRefreshToken(utilisateur);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRoles().stream().findFirst().map(r -> r.getNom().name()).orElse(null))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(utilisateur);
        RefreshToken refreshToken = createRefreshToken(utilisateur);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRoles().stream().findFirst().map(r -> r.getNom().name()).orElse(null))
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BadRequestException("Refresh token non trouvé ou invalide"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expiré. Veuillez vous reconnecter.");
        }

        Utilisateur user = refreshToken.getUser();
        String newToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken.getToken())
                .email(user.getEmail())
                .role(user.getRoles().stream().findFirst().map(r -> r.getNom().name()).orElse(null))
                .build();
    }

    private RefreshToken createRefreshToken(Utilisateur user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> {
                    RefreshToken rt = new RefreshToken();
                    rt.setUser(user);
                    return rt;
                });
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000)); // 7 jours d'expiration
        return refreshTokenRepository.save(refreshToken);
    }
}
