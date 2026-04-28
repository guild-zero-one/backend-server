package com.zeroone.simlady.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoResumoResponseDto {
    @Schema(description = "ID do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String status;

    @Schema(description = "ID da venda quando o pedido foi finalizado", example = "550e8400-e29b-41d4-a716-446655440099")
    private UUID idVenda;

    @Schema(description = "Data de criação do pedido", example = "2026-04-25T15:59:14")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização do pedido", example = "2026-04-25T16:10:01")
    private LocalDateTime atualizadoEm;

    @Schema(description = "Total de itens do pedido (somatório de quantidades)", example = "3")
    private Integer totalItens;

    @Schema(description = "Valor total de venda do pedido", example = "259.80")
    private BigDecimal totalValorVenda;

    @Schema(description = "Usuário resumido")
    private UsuarioResumoPedidoResponseDto usuario;
}
