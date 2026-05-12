package com.zeroone.simlady.config;

import com.zeroone.simlady.service.CloudStorageService;
import com.zeroone.simlady.service.LocalStorageService;
import com.zeroone.simlady.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StorageConfig implements WebMvcConfigurer {

    @Value("${storage.local.path:uploads/temp}")
    private String localPath;

    @Bean
    @ConditionalOnProperty(name = "storage.cloud.enabled", havingValue = "true")
    public StorageService cloudStorageService(
            @Value("${storage.cloud.access-key}") String accessKey,
            @Value("${storage.cloud.secret-key}") String secretKey,
            @Value("${storage.cloud.endpoint}") String endpoint,
            @Value("${storage.cloud.bucket}") String bucket,
            @Value("${storage.cloud.public-url}") String publicUrl,
            @Value("${storage.cloud.region:auto}") String region) {
        return new CloudStorageService(accessKey, secretKey, endpoint, bucket, publicUrl, region);
    }

    @Bean
    @ConditionalOnProperty(name = "storage.cloud.enabled", havingValue = "false", matchIfMissing = true)
    public StorageService localStorageService(
            @Value("${storage.local.base-url:http://localhost:8080/uploads}") String baseUrl) {
        return new LocalStorageService(localPath, baseUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + localPath + "/");
    }
}
