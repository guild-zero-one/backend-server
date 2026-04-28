package com.zeroone.simlady.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HomeKpisResponseDto {
    @Schema(description = "Soma da quantidade de todos os produtos em estoque", example = "512")
    private Long totalItensEstoque;

    @Schema(description = "Quantidade de pedidos com status PENDENTE", example = "20")
    private Integer pedidosPendentes;

    @Schema(description = "Quantidade de vendas com pagamento pendente", example = "7")
    private Long vendasPendentes;
}
