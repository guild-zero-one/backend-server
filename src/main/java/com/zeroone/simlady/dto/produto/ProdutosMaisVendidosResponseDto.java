package com.zeroone.simlady.dto.produto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutosMaisVendidosResponseDto {
    private Integer produtoId;
    private String nomeProduto;
    private Long totalVendido;
    private BigDecimal valorTotalVendido;

    public ProdutosMaisVendidosResponseDto(Integer produtoId, String nomeProduto, Long totalVendido, BigDecimal valorTotalVendido) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.totalVendido = totalVendido;
        this.valorTotalVendido = valorTotalVendido;
    }
}