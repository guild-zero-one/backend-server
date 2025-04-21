package com.zeroone.simlady.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoItemResponseDto {
    @Schema(description = "ID do Item", example = "1")
    private Integer id;

    @Schema(description = "ID do Produto", example = "1")
    private Integer idProduto;

    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço Unitário item", example = "49.99")
    private BigDecimal precoUnitario;
}