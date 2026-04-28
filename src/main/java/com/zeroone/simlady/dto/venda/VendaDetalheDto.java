package com.zeroone.simlady.dto.venda;

import com.zeroone.simlady.dto.pedido.PedidoDetalheVendaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class VendaDetalheDto {
    @Schema(description = "ID da Venda", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Valor total da venda", example = "259.80")
    private BigDecimal valorTotal;

    @Schema(description = "Desconto da venda", example = "0.00")
    private BigDecimal desconto;

    @Schema(description = "Pagamento realizado", example = "false")
    private Boolean pagamentoRealizado;

    @Schema(description = "Data da venda", example = "2026-04-25")
    private LocalDate dataVenda;

    @Schema(description = "Pedido completo associado à venda")
    private PedidoDetalheVendaDto pedido;
}

