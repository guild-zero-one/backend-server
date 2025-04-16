package com.zeroone.simlady.dto.produtoImagem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoImagemResponseDto {
    private Integer id;
    private String urlImagem;
    private Boolean imagemPrincipal;
    private Integer produtoId;
}
