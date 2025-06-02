package com.zeroone.simlady.service.bucket;

import java.io.InputStream;

public interface BucketService {
    String uploadImagem(InputStream imagemStream, long tamanho, String nomeArquivo, String contentType);
}
