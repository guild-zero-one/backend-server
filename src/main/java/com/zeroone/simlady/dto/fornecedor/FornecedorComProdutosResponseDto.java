package com.zeroone.simlady.dto.fornecedor;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FornecedorComProdutosResponseDto {
    private UUID id;
    private String nome;
    private String cnpj;
    private String descricao;
    private String imagemUrl;
    private List<ProdutoResponseDto> produtos;
}