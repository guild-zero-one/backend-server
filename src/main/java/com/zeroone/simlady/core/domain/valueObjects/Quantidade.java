package com.zeroone.simlady.core.domain.valueObjects;

public class Quantidade {
    private Integer value;

    public static Quantidade of(Integer value){
        if (value == null || !isQuantidadeValid(value)){
            throw new IllegalArgumentException("Quantidade Inválida: " + value);
        }
        return new Quantidade(value);
    }

    private static Boolean isQuantidadeValid(Integer value){
        return value != null && value >= 0;
    }

    private Quantidade(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
