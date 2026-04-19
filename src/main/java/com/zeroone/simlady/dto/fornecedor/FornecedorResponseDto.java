package com.zeroone.simlady.dto.fornecedor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
public class FornecedorResponseDto {
    @Schema(description = "ID do fornecedor/marca", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nome do fornecedor/marca", example = "Boticário")
    private String nome;

    @Schema(description = "CNPJ", example = "10.164.222/0001-01")
    private String cnpj;

    private String descricao;
    private String imagemUrl;

    @Schema(description = "Total de produtos do fornecedor", example = "8")
    private Long totalProdutos;
}
