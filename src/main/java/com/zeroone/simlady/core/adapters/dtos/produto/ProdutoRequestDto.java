package com.zeroone.simlady.core.adapters.dtos.produto;

public record ProdutoRequestDto(String nome, String sku, String descricao, String tag, Integer quantidade, Double precoUnitario, Boolean catalogo, Double valorVenda ) {
}
