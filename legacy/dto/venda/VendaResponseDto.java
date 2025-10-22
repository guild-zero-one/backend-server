package com.zeroone.simlady.dto.venda;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VendaResponseDto {
    @Schema(description = "ID da Venda", example = "1")
    private Integer id;

    @Schema(description = "Valor total da venda", example = "150.00")
    private BigDecimal valorTotal;

    @Schema(description = "Desconto da venda", example = "30.00")
    private BigDecimal desconto;

    @Schema(description = "Pagamento realizado", example = "true")
    private Boolean pagamentoRealizado;

    @Schema(description = "Data da venda", example = "20/05/2026")
    private LocalDate dataVenda;
}
