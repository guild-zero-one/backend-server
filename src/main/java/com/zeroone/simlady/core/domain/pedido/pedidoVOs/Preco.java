package com.zeroone.simlady.core.domain.pedido.pedidoVOs;

import java.math.BigDecimal;

public class Preco {
    private final BigDecimal valor;

    private Preco(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço deve ser maior ou igual a zero");
        }
        this.valor = valor;
    }

    public static Preco of(BigDecimal valor) {
        return new Preco(valor);
    }

    public static Preco of(String valor) {
        return new Preco(new BigDecimal(valor));
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Preco multiplicar(Quantidade quantidade) {
        return new Preco(valor.multiply(BigDecimal.valueOf(quantidade.getValor())));
    }

    public Preco somar(Preco outro) {
        return new Preco(valor.add(outro.valor));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Preco preco = (Preco) obj;
        return valor.equals(preco.valor);
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
