package com.zeroone.simlady.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Slf4j
public class CloudStorageService implements StorageService {

    private final S3Client s3Client;
    private final String bucket;
    private final String publicUrl;

    public CloudStorageService(String accessKey, String secretKey, String endpoint,
                               String bucket, String publicUrl, String region) {
        this.bucket = bucket;
        this.publicUrl = publicUrl;
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @PostConstruct
    public void healthCheck() {
        try {
            s3Client.headBucket(r -> r.bucket(bucket));
            log.info("[Storage] Conexão com cloud storage estabelecida. Bucket: {}", bucket);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "[STORAGE] Falha ao conectar ao cloud storage. Verifique as credenciais.", e);
        }
    }

    @Override
    public String upload(String folder, String filename, byte[] bytes, String mimeType) {
        String key = folder + "/" + filename;
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(mimeType)
                            .contentLength((long) bytes.length)
                            .build(),
                    RequestBody.fromBytes(bytes)
            );
            return publicUrl + "/" + key;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar imagem para o storage. Tente novamente.", e);
        }
    }

    @Override
    public void delete(String fileKey) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileKey)
                        .build()
        );
    }

    @Override
    public String getUrl(String fileKey) {
        return publicUrl + "/" + fileKey;
    }
}
