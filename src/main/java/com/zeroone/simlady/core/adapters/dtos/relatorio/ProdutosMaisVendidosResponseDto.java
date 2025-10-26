package com.zeroone.simlady.core.adapters.dtos.relatorio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutosMaisVendidosResponseDto {
    private UUID id;
    private String nome;
    private Integer quantidadeVendida;
    private BigDecimal valorTotalVendido;
}
