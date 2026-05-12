package com.zeroone.simlady.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class LocalStorageService implements StorageService {

    private final String localPath;
    private final String baseUrl;

    public LocalStorageService(String localPath, String baseUrl) {
        this.localPath = localPath;
        this.baseUrl = baseUrl;
    }

    @Override
    public String upload(String folder, String filename, byte[] bytes, String mimeType) {
        try {
            Path dir = Paths.get(localPath, folder);
            Files.createDirectories(dir);
            Files.write(dir.resolve(filename), bytes);
            return baseUrl + "/" + folder + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo localmente.", e);
        }
    }

    @Override
    public void delete(String fileKey) {
        try {
            Path filePath = Paths.get(localPath).resolve(fileKey);
            boolean deleted = Files.deleteIfExists(filePath);
            if (!deleted) {
                log.warn("[Storage] Arquivo não encontrado para exclusão: {}", fileKey);
            }
        } catch (IOException e) {
            log.error("[Storage] Falha ao deletar arquivo local: {}", fileKey, e);
        }
    }

    @Override
    public String getUrl(String fileKey) {
        return baseUrl + "/" + fileKey;
    }
}
