package com.zeroone.simlady.core.domain.fornecedor.fornecedorVOs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class ImagemUrl {

    private final String url;

    private ImagemUrl(String url) {
        this.url = url;
    }

    public static ImagemUrl of(String url) {
        Objects.requireNonNull(url, "URL da imagem não pode ser nula");
        validarUrl(url);
        return new ImagemUrl(url);
    }

    private static void validarUrl(String url) {
        try {
            URL newurl = new URL(url);
            if (!(newurl.getProtocol().equals("http") || newurl.getProtocol().equals("https"))) {
                throw new IllegalArgumentException("Protocolo invalido na URL da imagem: " + url);
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL da imagem invalida: " + url, e);
        }
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagemUrl)) return false;
        ImagemUrl that = (ImagemUrl) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}

