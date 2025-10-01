package com.zeroone.simlady.core.adapters.dtos.produto;

public record ProdutoResponseDto(String id, String nome, String sku, String descricao,String tag, Integer quantidade, Double precoUnitario, Boolean catalogo, Double valorVenda) {
}
