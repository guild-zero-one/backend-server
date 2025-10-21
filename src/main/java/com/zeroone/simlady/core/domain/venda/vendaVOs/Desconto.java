package com.zeroone.simlady.core.domain.venda.vendaVOs;

import java.math.BigDecimal;

public class Desconto {
    private final BigDecimal valor;

    private Desconto(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Desconto deve ser maior ou igual a zero");
        }
        this.valor = valor;
    }

    public static Desconto of(BigDecimal valor) {
        return new Desconto(valor);
    }

    public static Desconto of(String valor) {
        return new Desconto(new BigDecimal(valor));
    }

    public static Desconto zero() {
        return new Desconto(BigDecimal.ZERO);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public boolean isZero() {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Desconto desconto = (Desconto) obj;
        return valor.equals(desconto.valor);
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
