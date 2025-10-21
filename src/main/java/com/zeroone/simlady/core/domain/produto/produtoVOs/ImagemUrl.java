package com.zeroone.simlady.core.domain.produto.produtoVOs;

import java.util.regex.Pattern;

public class ImagemUrl {
    private static final Pattern URL_REGEX = Pattern.compile(
            "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$"
    );

    private final String value;

    public static ImagemUrl of(String value) {
        if (!isImagemUrlValid(value)) {
            throw new IllegalArgumentException("URL de imagem inválida: " + value);
        }
        return new ImagemUrl(value);
    }

    private static Boolean isImagemUrlValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return URL_REGEX.matcher(value).matches();
    }

    private ImagemUrl(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImagemUrl imagemUrl = (ImagemUrl) obj;
        return value.equals(imagemUrl.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
