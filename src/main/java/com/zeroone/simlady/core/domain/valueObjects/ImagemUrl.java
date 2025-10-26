package com.zeroone.simlady.core.domain.valueObjects;

public class ImagemUrl {
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
        return true; // Aceita qualquer URL não vazia
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
