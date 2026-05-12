package com.zeroone.simlady.service;

public interface StorageService {

    String upload(String folder, String filename, byte[] bytes, String mimeType);

    void delete(String fileKey);

    String getUrl(String fileKey);
}
