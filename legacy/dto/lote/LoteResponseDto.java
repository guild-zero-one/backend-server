package com.zeroone.simlady.dto.lote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoteResponseDto {
    @Schema(description = "ID do Lote", example = "1")
    private Integer id;

    @Schema(description = "Quantidade total de produtos", example = "12")
    private Integer qtdLote;

    @Schema(description = "Valor total de produtos", example = "120.00")
    private Double valorTotal;
}
