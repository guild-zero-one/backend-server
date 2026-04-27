package com.zeroone.simlady.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class ProdutoResumoPedidoResponseDto {
    @Schema(description = "ID do produto", example = "550e8400-e29b-41d4-a716-446655440020")
    private UUID id;

    @Schema(description = "Nome do produto", example = "Perfume Malbec")
    private String nome;

    @Schema(description = "URL da imagem do produto", example = "https://example.com/produto.png")
    private String urlImagem;

    @Schema(description = "Descrição do produto", example = "Fragrância amadeirada")
    private String descricao;
}
