package com.zeroone.simlady.core.adapters.dtos.relatorio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumoVendasProdutoResponseDto {
    private Integer vendasMesAtual;
    private Integer vendasTotais;
}
