package com.Groupe3.API_REST_spring.boot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String uploadPhoto(MultipartFile file, Long etudiantId) throws IOException;
    String uploadDocument(MultipartFile file, Long etudiantId) throws IOException;
    byte[] getPhoto(String fileName) throws IOException;
    byte[] getDocument(String fileName) throws IOException;
}
