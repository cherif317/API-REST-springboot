# École API - Système de Gestion Scolaire

Une API REST complète pour la gestion d'une école, développée avec Spring Boot, Spring Security, JWT, et JPA.

## 📋 Table des Matières

- [Fonctionnalités](#fonctionnalités)
- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Sécurité](#sécurité)
- [Swagger UI](#swagger-ui)

## ✨ Fonctionnalités

- **Authentification JWT**
  - Inscription d'utilisateurs avec rôles (ADMIN, ENSEIGNANT, ETUDIANT)
  - Login avec génération de token JWT
  - Protection des endpoints avec Spring Security

- **Gestion des Étudiants**
  - CRUD complet pour les étudiants
  - Pagination et recherche
  - Upload de photo de profil (max 2Mo)
  - Upload de documents PDF

- **Gestion des Cours**
  - CRUD complet pour les cours
  - Association avec des enseignants
  - Pagination et recherche

- **Gestion des Inscriptions**
  - Inscription des étudiants aux cours
  - Consultation des inscriptions par étudiant ou cours
  - Prévention des doublons

- **Gestion des Exceptions**
  - Gestion centralisée des erreurs
  - Messages d'erreur détaillés
  - Validation des données

## 🛠 Technologies

- **Java 21**
- **Spring Boot 3.5.15**
- **Spring Security** - Authentification et autorisation
- **Spring Data JPA** - Accès aux données
- **MySQL** - Base de données
- **JWT (jjwt)** - Gestion des tokens
- **MapStruct** - Mapping Entity-DTO
- **Lombok** - Réduction du code boilerplate
- **SpringDoc OpenAPI** - Documentation Swagger
- **Maven** - Gestion des dépendances

## 📦 Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Installation

1. **Cloner le repository**
   ```bash
   git clone <repository-url>
   cd Eduplus
   ```

2. **Configurer la base de données MySQL**
   ```sql
   CREATE DATABASE ecole;
   ```

3. **Configurer les propriétés de l'application**
   
   Modifier `src/main/resources/application.yml` avec vos credentials MySQL:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/ecole
       username: root
       password: votre_mot_de_passe
   ```

4. **Compiler le projet**
   ```bash
   mvn clean install
   ```

5. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

L'application sera accessible sur `http://localhost:8080`

## ⚙️ Configuration

### Configuration JWT

Les paramètres JWT sont configurés dans `application.yml`:

```yaml
jwt:
  secret: votre_secret_key
  expiration: 86400000  # 24 heures en millisecondes
```

### Configuration Upload

Les fichiers uploadés sont stockés dans le dossier `uploads`:

```yaml
app:
  upload:
    dir: uploads
```

### Configuration CORS

Les origines autorisées sont configurées dans `CorsConfig.java`:
- `http://localhost:4200` (Angular)
- `http://localhost:3000` (React)

## 📡 API Endpoints

### Authentification

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| POST | `/api/auth/register` | Inscription d'un utilisateur | Public |
| POST | `/api/auth/login` | Connexion d'un utilisateur | Public |

**Exemple Register:**
```json
{
  "nom": "Doe",
  "prenom": "John",
  "email": "john@example.com",
  "motDePasse": "password123",
  "role": "ETUDIANT"
}
```

**Exemple Login:**
```json
{
  "email": "john@example.com",
  "motDePasse": "password123"
}
```

### Étudiants

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| GET | `/api/etudiants` | Liste paginée des étudiants | ADMIN, ENSEIGNANT |
| GET | `/api/etudiants/search?nom=xxx` | Recherche par nom | ADMIN, ENSEIGNANT |
| GET | `/api/etudiants/{id}` | Détails d'un étudiant | ADMIN, ENSEIGNANT, ETUDIANT |
| POST | `/api/etudiants` | Créer un étudiant | ADMIN |
| PUT | `/api/etudiants/{id}` | Modifier un étudiant | ADMIN |
| DELETE | `/api/etudiants/{id}` | Supprimer un étudiant | ADMIN |

### Cours

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| GET | `/api/cours` | Liste des cours | Tous authentifiés |
| GET | `/api/cours/search?nom=xxx` | Recherche par nom | Tous authentifiés |
| GET | `/api/cours/{id}` | Détails d'un cours | Tous authentifiés |
| POST | `/api/cours` | Créer un cours | ADMIN |
| PUT | `/api/cours/{id}` | Modifier un cours | ADMIN |
| DELETE | `/api/cours/{id}` | Supprimer un cours | ADMIN |

### Inscriptions

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| GET | `/api/inscriptions` | Liste des inscriptions | ADMIN |
| GET | `/api/inscriptions/{id}` | Détails d'une inscription | ADMIN |
| GET | `/api/inscriptions/etudiant/{id}` | Inscriptions d'un étudiant | ADMIN, ENSEIGNANT, ETUDIANT |
| GET | `/api/inscriptions/cours/{id}` | Inscriptions à un cours | ADMIN |
| POST | `/api/inscriptions?etudiantId=xxx&coursId=xxx` | Inscrire un étudiant | ADMIN |
| DELETE | `/api/inscriptions/{id}` | Supprimer une inscription | ADMIN |

### Upload

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| POST | `/api/upload/etudiants/{id}/photo` | Upload photo de profil | ADMIN, ETUDIANT |
| POST | `/api/upload/etudiants/{id}/docs` | Upload document PDF | ADMIN, ETUDIANT |
| GET | `/api/upload/photos/{fileName}` | Récupérer une photo | Public |
| GET | `/api/upload/documents/{fileName}` | Récupérer un document | Public |

## 🔒 Sécurité

### Rôles

- **ADMIN** - Accès complet à toutes les opérations
- **ENSEIGNANT** - Accès en lecture aux étudiants et cours
- **ETUDIANT** - Accès limité à ses propres données

### Utilisation du Token JWT

Pour accéder aux endpoints protégés, incluez le token dans l'en-tête Authorization:

```
Authorization: Bearer <votre_token_jwt>
```

## 📚 Swagger UI

La documentation interactive de l'API est disponible via Swagger UI:

**URL:** `http://localhost:8080/swagger-ui.html`

Swagger UI permet de:
- Visualiser tous les endpoints
- Tester les API directement
- Voir les schémas de requête/réponse
- Télécharger la spécification OpenAPI

## 🗂 Structure du Projet

```
src/main/java/com/Groupe3/API_REST_spring/boot/
├── config/              # Configuration (Security, JWT, CORS)
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
│   ├── auth/
│   ├── cours/
│   ├── etudiant/
│   └── inscription/
├── entity/              # Entités JPA
├── enums/               # Énumérations
├── exception/           # Gestion des exceptions
├── mapper/              # MapStruct Mappers
├── repository/          # Spring Data JPA Repositories
├── security/            # Security (Filter, UserDetailsService)
├── service/             # Business Logic
│   └── impl/
└── Util/                # Utilitaires
```

## 🧪 Tests

Pour exécuter les tests:

```bash
mvn test
```

## 📝 Notes Importantes

- Le schéma de la base de données est généré automatiquement par Hibernate (ddl-auto=update)
- Les rôles par défaut sont insérés via `data.sql`
- La taille maximale des fichiers uploadés est de 2Mo
- Les tokens JWT expirent après 24 heures
- Les mots de passe sont encodés avec BCrypt

## 🐛 Dépannage

### Problème de connexion à la base de données

Vérifiez que:
- MySQL est en cours d'exécution
- La base de données `ecole` existe
- Les credentials dans `application.yml` sont corrects

### Erreur de token JWT

Assurez-vous que:
- Le token est inclus dans l'en-tête Authorization
- Le token n'est pas expiré
- Le secret JWT est le même côté serveur

### Erreur d'upload de fichier

Vérifiez que:
- Le dossier `uploads` existe
- Le fichier ne dépasse pas 2Mo
- Le type MIME est autorisé (image/jpeg, image/png, application/pdf)

## 👥 Auteurs

- Groupe 3 - STI L3

## 📄 Licence

Ce projet est développé dans un cadre académique.
