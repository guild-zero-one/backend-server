package com.zeroone.simlady.core.adapters.dtos.venda;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VendaResponseDto {
    private UUID id;
    private String valorTotal;
    private String desconto;
    private String valorFinal;
    private Boolean pagamentoRealizado;
    private LocalDate dataVenda;
    private List<UUID> pedidosIds;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
