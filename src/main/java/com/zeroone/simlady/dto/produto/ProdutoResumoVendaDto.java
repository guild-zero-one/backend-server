package com.zeroone.simlady.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class ProdutoResumoVendaDto {
    @Schema(description = "ID do Produto", example = "550e8400-e29b-41d4-a716-446655440020")
    private UUID id;

    @Schema(description = "Nome do produto", example = "Perfume Malbec")
    private String nome;

    @Schema(description = "URL da imagem do produto", example = "https://...")
    private String urlImagem;

    @Schema(description = "Descrição do produto", example = "Fragrância amadeirada")
    private String descricao;
}

