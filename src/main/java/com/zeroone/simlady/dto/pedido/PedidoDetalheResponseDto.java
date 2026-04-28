package com.zeroone.simlady.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoDetalheResponseDto extends PedidoResumoResponseDto {
    @Schema(description = "Custo total baseado em precoUnitario dos itens", example = "250.00")
    private BigDecimal totalPrecoUnitario;

    @Schema(description = "Itens detalhados do pedido")
    private List<PedidoItemDetalheResponseDto> itens;
}
