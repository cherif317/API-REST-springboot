package com.Groupe3.API_REST_spring.boot.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads";

    public static String saveFile(MultipartFile file, String subDirectory) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR, subDirectory);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        return newFilename;
    }

    public static byte[] getFile(String subDirectory, String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, subDirectory, filename);
        if (!Files.exists(filePath)) {
            throw new IOException("Fichier non trouvé: " + filename);
        }
        return Files.readAllBytes(filePath);
    }

    public static void deleteFile(String subDirectory, String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, subDirectory, filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
