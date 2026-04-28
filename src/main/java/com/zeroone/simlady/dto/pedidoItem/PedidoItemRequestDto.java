package com.zeroone.simlady.dto.pedidoItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PedidoItemRequestDto {
    @NotNull(message = "ID do Produto não pode ser nulo.")
    @Schema(description = "ID do Produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idProduto;

    @NotNull(message = "Quantidade não pode ser nulo.")
    @Positive(message = "Quantidade deve ser um número positivo.")
    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantidade;

    @NotNull(message = "Preço Unitário não pode ser nulo.")
    @PositiveOrZero(message = "Preço Unitário deve ser um número positivo ou zero.")
    @Schema(description = "Preço unitário item", example = "49.99")
    private BigDecimal precoUnitario;

    @NotNull(message = "Valor de venda não pode ser nulo.")
    @PositiveOrZero(message = "Valor de venda deve ser um número positivo ou zero.")
    @Schema(description = "Valor de venda do item", example = "59.99")
    private BigDecimal valorVenda;
}
