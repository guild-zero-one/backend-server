package com.zeroone.simlady.core.domain.venda.vendaVOs;

import java.math.BigDecimal;

public class ValorTotal {
    private final BigDecimal valor;

    private ValorTotal(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor total não pode ser nulo");
        }
        this.valor = valor;
    }

    public static ValorTotal of(BigDecimal valor) {
        return new ValorTotal(valor);
    }

    public static ValorTotal of(String valor) {
        return new ValorTotal(new BigDecimal(valor));
    }

    public BigDecimal getValor() {
        return valor;
    }

    public ValorTotal somar(ValorTotal outro) {
        return new ValorTotal(valor.add(outro.valor));
    }

    public ValorTotal subtrair(ValorTotal outro) {
        return new ValorTotal(valor.subtract(outro.valor));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValorTotal that = (ValorTotal) obj;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
