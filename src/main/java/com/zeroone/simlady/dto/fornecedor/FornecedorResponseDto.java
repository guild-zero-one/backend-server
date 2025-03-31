package com.zeroone.simlady.dto.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FornecedorResponseDto {
    private Integer id;
    private String nome;
    private String cnpj;
}
