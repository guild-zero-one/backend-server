package com.zeroone.simlady.core.domain.valueObjects;

public class Quantidade {
    private Integer value;

    public static Quantidade of(Integer value){
        if (!isQuantidadeValid(value)){
            throw new IllegalArgumentException("Preço Inválido: " + value);
        }
        return new Quantidade(value);
    }

    private static Boolean isQuantidadeValid(Integer value){
        return value > 0.0;
    }

    private Quantidade(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
