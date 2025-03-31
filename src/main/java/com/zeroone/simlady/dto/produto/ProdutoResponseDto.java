package com.zeroone.simlady.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoResponseDto {
    private Integer id;
    private String nome;
    private String nomeFantasia;
    private Integer fornecedorId;
}
