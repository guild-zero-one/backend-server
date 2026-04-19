package com.zeroone.simlady.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDto {
    @Schema(description = "Nome da categoria", example = "Masculino")
    @NotBlank(message = "Nome não pode ser vazio.")
    private String nome;

    @Schema(description = "Descrição da categoria", example = "Produtos para o público masculino")
    private String descricao;
}


