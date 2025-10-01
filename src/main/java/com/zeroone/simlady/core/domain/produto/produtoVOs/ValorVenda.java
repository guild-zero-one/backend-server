package com.zeroone.simlady.core.domain.produto.produtoVOs;

public class ValorVenda {
    private Double value;

    public static ValorVenda of(Double value){
        if (!isValorVendaValid(value)){
            throw new IllegalArgumentException("Valor de Venda Inválido: " + value);
        }
        return new ValorVenda(value);
    }

    private static Boolean isValorVendaValid(Double value){
        return value > 0.0;
    }

    private ValorVenda(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

}
