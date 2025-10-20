package com.zeroone.simlady.core.adapters.dtos.fornecedor;

public record FornecedorRequestDto(
        String nome,
        String descricao,
        String cnpj,
        String imagemUrl
) {}
