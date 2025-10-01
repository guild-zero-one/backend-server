package com.zeroone.simlady.core.domain.produto.produtoVOs;

import java.util.regex.Pattern;

public class Sku {
    private static final Pattern SKU_REGEX = Pattern.compile("^[A-Z0-9\\-]{1,50}$");

    private final String value;

    public static Sku of(String value){
        if (!isSkuValid(value)){
            throw new IllegalArgumentException("SKU inválido: " + value);
        }
        return new Sku(value);
    }

    private static Boolean isSkuValid(String value){
        return SKU_REGEX.matcher(value).matches();
    }

    private Sku(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
