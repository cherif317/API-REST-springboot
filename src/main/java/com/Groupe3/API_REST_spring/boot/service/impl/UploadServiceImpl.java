package com.Groupe3.API_REST_spring.boot.service.impl;

import com.Groupe3.API_REST_spring.boot.exception.BadRequestException;
import com.Groupe3.API_REST_spring.boot.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList("application/pdf");
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    @Override
    public String uploadPhoto(MultipartFile file, Long etudiantId) throws IOException {
        validateFile(file, ALLOWED_IMAGE_TYPES);
        
        String fileName = generateFileName(file.getOriginalFilename(), etudiantId, "photo");
        Path uploadPath = Paths.get(uploadDir, "photos");
        
        ensureDirectoryExists(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return fileName;
    }

    @Override
    public String uploadDocument(MultipartFile file, Long etudiantId) throws IOException {
        validateFile(file, ALLOWED_DOCUMENT_TYPES);
        
        String fileName = generateFileName(file.getOriginalFilename(), etudiantId, "doc");
        Path uploadPath = Paths.get(uploadDir, "documents");
        
        ensureDirectoryExists(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return fileName;
    }

    @Override
    public byte[] getPhoto(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, "photos", fileName);
        if (!Files.exists(filePath)) {
            throw new BadRequestException("Photo non trouvée");
        }
        return Files.readAllBytes(filePath);
    }

    @Override
    public byte[] getDocument(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, "documents", fileName);
        if (!Files.exists(filePath)) {
            throw new BadRequestException("Document non trouvé");
        }
        return Files.readAllBytes(filePath);
    }

    private void validateFile(MultipartFile file, List<String> allowedTypes) {
        if (file.isEmpty()) {
            throw new BadRequestException("Le fichier est vide");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("La taille du fichier dépasse la limite de 2MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new BadRequestException("Type de fichier non autorisé");
        }
    }

    private String generateFileName(String originalFilename, Long etudiantId, String type) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return etudiantId + "_" + type + "_" + UUID.randomUUID().toString() + extension;
    }

    private void ensureDirectoryExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
