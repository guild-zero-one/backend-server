package com.zeroone.simlady.dto.pedidoItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PedidoItemResponseDto {
    @Schema(description = "ID do Item", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID do Produto", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID idProduto;

    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço Unitário item", example = "49.99")
    private BigDecimal precoUnitario;

    @Schema(description = "Valor de venda do item no momento do pedido", example = "79.99")
    private BigDecimal valorVenda;
}
