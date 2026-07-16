-- Insertion des rôles par défaut
INSERT INTO roles (nom) VALUES ('ADMIN'), ('ENSEIGNANT'), ('ETUDIANT');

-- Insertion d'un utilisateur admin par défaut (mot de passe: admin123)
-- Le mot de passe doit être encodé en BCrypt avant insertion
-- Pour l'instant, cette insertion est commentée - vous pouvez l'activer après avoir généré le hash BCrypt
-- INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, compte_active, compte_verifie, date_creation) 
-- VALUES ('Admin', 'System', 'admin@ecole.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', true, true, NOW());

-- Insertion des rôles pour l'admin
-- INSERT INTO utilisateur_roles (utilisateur_id, role_id) 
-- VALUES (1, 1);
