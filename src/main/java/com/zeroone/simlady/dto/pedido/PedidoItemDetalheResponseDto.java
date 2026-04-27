package com.zeroone.simlady.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PedidoItemDetalheResponseDto {
    @Schema(description = "ID do item", example = "550e8400-e29b-41d4-a716-446655440010")
    private UUID id;

    @Schema(description = "Quantidade do item", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário no momento da criação do pedido", example = "100.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Valor de venda no momento da criação do pedido", example = "129.90")
    private BigDecimal valorVenda;

    @Schema(description = "Produto resumido")
    private ProdutoResumoPedidoResponseDto produto;
}
