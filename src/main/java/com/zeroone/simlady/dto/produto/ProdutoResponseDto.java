package com.zeroone.simlady.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProdutoResponseDto {
    @Schema(description = "ID do Produto", example = "550e8400-e29b-41d4-a716-446655440020")
    private UUID id;

    @Schema(description = "Nome do produto", example = "Malbec Perfume")
    private String nome;

    @Schema(description = "SKU do produto", example = "MAL-PER-100")
    private String sku;

    @Schema(description = "Descrição do produto", example = "O melhor perfume do Brasil")
    private String descricao;

    @Schema(description = "Quantidade de produtos", example = "12")
    private Integer quantidade;

    @Schema(description = "URL da imagem do produto", example = "https://boticario-malbec.png")
    private String urlImagem;

    @Schema(description = "Preço unitário de compra", example = "30.00")
    private Double precoUnitario;

    @Schema(description = "Exibição do produto no site", example = "true")
    private Boolean catalogo;

    @Schema(description = "Valor de venda", example = "50.00")
    private Double valorVenda;

    @Schema(description = "ID do fornecedor/marca", example = "550e8400-e29b-41d4-a716-446655440010")
    private UUID fornecedorId;

    @Schema(description = "Data de criação do produto", example = "2024-01-15T10:30:00")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização do produto", example = "2024-03-20T14:22:00")
    private LocalDateTime atualizadoEm;
}
