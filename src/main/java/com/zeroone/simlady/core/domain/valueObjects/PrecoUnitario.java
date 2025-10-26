package com.zeroone.simlady.core.domain.valueObjects;


public class PrecoUnitario {
    private Double value;

    public static PrecoUnitario of(Double value){
        if (value == null || !isPrecoValid(value)){
            throw new IllegalArgumentException("Preço Inválido: " + value);
        }
        return new PrecoUnitario(value);
    }

    private static Boolean isPrecoValid(Double value){
        return value != null && value > 0.0;
    }

    private PrecoUnitario(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

}
