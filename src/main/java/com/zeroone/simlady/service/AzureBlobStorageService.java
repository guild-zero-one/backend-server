package com.zeroone.simlady.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobStorageService {

    private final BlobContainerClient containerClient;

    public AzureBlobStorageService() {
        String connectionString = "";
        String containerName = "imagens";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
    }

    public String uploadImagem(InputStream imagemStream, long tamanho, String nomeArquivo, String contentType) {
        BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
        blobClient.upload(imagemStream, tamanho, true);
        blobClient.setHttpHeaders(headers);
        return blobClient.getBlobUrl(); // URL pública se o container for público
    }
}