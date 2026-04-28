package com.zeroone.simlady.dto.venda;

import com.zeroone.simlady.dto.pedido.PedidoDetalheResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class VendaResponseDto {
    @Schema(description = "ID da Venda", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Valor total da venda", example = "150.00")
    private BigDecimal valorTotal;

    @Schema(description = "Desconto da venda", example = "30.00")
    private BigDecimal desconto;

    @Schema(description = "Pagamento realizado", example = "true")
    private Boolean pagamentoRealizado;

    @Schema(description = "Data da venda", example = "20/05/2026")
    private LocalDate dataVenda;

    @Schema(description = "Usuário resumido relacionado à venda")
    private UsuarioResumoVendaResponseDto usuario;

    @Schema(description = "Pedido completo embutido no detalhe da venda")
    private PedidoDetalheResponseDto pedido;
}
