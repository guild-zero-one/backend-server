package com.zeroone.simlady.service.bucket;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobStorageService implements BucketService {

    private final BlobContainerClient containerClient;

    public AzureBlobStorageService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @CircuitBreaker(name = "azureBlobStorage", fallbackMethod = "fallbackUploadImagem")
    public String uploadImagem(InputStream imagemStream, long tamanho, String nomeArquivo, String contentType) {
        BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
        blobClient.upload(imagemStream, tamanho, true);
        blobClient.setHttpHeaders(headers);
        return blobClient.getBlobUrl();
    }

    public String fallbackUploadImagem(InputStream imagemStream, long tamanho, String nomeArquivo, String contentType, Throwable t){
        System.err.println("Falha ao fazer upload no Azure Blob Storage: " + t.getMessage());
        return null;
    }
}