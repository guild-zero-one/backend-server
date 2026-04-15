package com.zeroone.simlady.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoriaResponseDto {
    @Schema(description = "ID da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nome da categoria", example = "Masculino")
    private String nome;

    @Schema(description = "Descrição da categoria", example = "Produtos para o público masculino")
    private String descricao;
}

