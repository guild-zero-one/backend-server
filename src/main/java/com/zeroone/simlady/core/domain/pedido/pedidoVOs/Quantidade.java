package com.zeroone.simlady.core.domain.pedido.pedidoVOs;

public class Quantidade {
    private final Integer valor;

    private Quantidade(Integer valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.valor = valor;
    }

    public static Quantidade of(Integer valor) {
        return new Quantidade(valor);
    }

    public Integer getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quantidade that = (Quantidade) obj;
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
