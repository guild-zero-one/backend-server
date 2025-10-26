package com.zeroone.simlady.core.adapters.dtos.produto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProdutoResponseDto {
    private UUID id;
    private String nome;
    private String sku;
    private String descricao;
    private String tag;
    private Integer quantidade;
    private Double precoUnitario;
    private Boolean catalogo;
    private Double valorVenda;
    private String imagemUrl;
    private UUID idMarca;
}
