package com.zeroone.simlady.dto.fornecedor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class FornecedorResponseDto {
    @Schema(description = "ID do fornecedor/marca", example = "1")
    private Integer id;

    @Schema(description = "Nome do fornecedor/marca", example = "Boticário")
    private String nome;

    @Schema(description = "CNPJ", example = "10.164.222/0001-01")
    private String cnpj;
}
