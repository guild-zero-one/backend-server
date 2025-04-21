package com.zeroone.simlady.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoItemRequestDto {
    @NotNull(message = "ID do Produto não pode ser nulo.")
    @Schema(description = "ID do Produto", example = "1")
    private Integer idProduto;

    @NotNull(message = "Quantidade não pode ser nulo.")
    @Positive(message = "Quantidade deve ser um número positivo.")
    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantidade;

    @NotNull(message = "Preço Unitário não pode ser nulo.")
    @Positive(message = "Preço Unitário deve ser um número positivo.")
    @Schema(description = "Preço unitário item", example = "49.99")
    private BigDecimal precoUnitario;
}
