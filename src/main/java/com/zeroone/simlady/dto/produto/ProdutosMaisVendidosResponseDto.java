package com.zeroone.simlady.dto.produto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProdutosMaisVendidosResponseDto {
    private UUID produtoId;
    private String nomeProduto;
    private Long totalVendido;
    private BigDecimal valorTotalVendido;

    public ProdutosMaisVendidosResponseDto(UUID produtoId, String nomeProduto, Long totalVendido, BigDecimal valorTotalVendido) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.totalVendido = totalVendido;
        this.valorTotalVendido = valorTotalVendido;
    }
}