package com.example.aluguelcarros.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}") // Pasta configurada no application.properties
    private String uploadDir;

    /**
     * Salva um arquivo no diretório de uploads e retorna o nome gerado.
     * @param file Arquivo enviado (MultipartFile)
     * @return Nome único do arquivo salvo (ex: "a1b2c3d4-foto.jpg")
     * @throws IOException Se houver erro ao salvar o arquivo.
     */
    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio.");
        }

        // Cria o diretório se não existir
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gera um nome único para evitar sobrescrita
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Salva o arquivo no disco
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    /**
     * (OPCIONAL) Método para deletar um arquivo do diretório.
     */
    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }
}