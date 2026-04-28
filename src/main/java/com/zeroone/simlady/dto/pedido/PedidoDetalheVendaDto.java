package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemComProdutoDto;
import com.zeroone.simlady.dto.usuario.UsuarioResumoVendaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoDetalheVendaDto {
    @Schema(description = "ID do Pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Status do pedido", example = "CONCLUIDO")
    private String status;

    @Schema(description = "ID da Venda", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID idVenda;

    @Schema(description = "Data de criação do pedido", example = "2026-04-25T12:38:11")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização do pedido", example = "2026-04-25T12:38:11")
    private LocalDateTime atualizadoEm;

    @Schema(description = "Total de itens do pedido", example = "2")
    private Integer totalItens;

    @Schema(description = "Total do preço unitário (custo)", example = "250.00")
    private BigDecimal totalPrecoUnitario;

    @Schema(description = "Total de valor de venda", example = "379.80")
    private BigDecimal totalValorVenda;

    @Schema(description = "Usuário que fez o pedido")
    private UsuarioResumoVendaDto usuario;

    @Schema(description = "Itens do pedido com produtos")
    private List<PedidoItemComProdutoDto> itens;
}

