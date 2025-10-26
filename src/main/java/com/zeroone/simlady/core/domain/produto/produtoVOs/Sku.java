package com.zeroone.simlady.core.domain.produto.produtoVOs;

public class Sku {
    private final String value;

    public static Sku of(String value){
        return new Sku(value);
    }

    private Sku(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
