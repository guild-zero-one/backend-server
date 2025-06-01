package com.zeroone.simlady.dto.relatorio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResumoVendasProdutoResponseDto {
    @Schema(description = "Quantidade de vendas no mês atual", example = "10")
    private Integer vendasMesAtual;

    @Schema(description = "Total de vendas acumuladas", example = "150")
    private Integer vendasTotais;
}