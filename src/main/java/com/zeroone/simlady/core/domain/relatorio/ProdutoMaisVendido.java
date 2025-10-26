package com.zeroone.simlady.core.domain.relatorio;

import java.math.BigDecimal;
import java.util.UUID;

public class ProdutoMaisVendido {
    private UUID id;
    private String nome;
    private Integer quantidadeVendida;
    private BigDecimal valorTotalVendido;

    private ProdutoMaisVendido(UUID id, String nome, Integer quantidadeVendida, BigDecimal valorTotalVendido) {
        this.id = id;
        this.nome = nome;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotalVendido = valorTotalVendido;
    }

    public static ProdutoMaisVendido of(UUID id, String nome, Integer quantidadeVendida, BigDecimal valorTotalVendido) {
        return new ProdutoMaisVendido(id, nome, quantidadeVendida, valorTotalVendido);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public BigDecimal getValorTotalVendido() {
        return valorTotalVendido;
    }
}
