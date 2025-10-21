package com.zeroone.simlady.core.adapters.dtos.venda;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VendaUpdateRequestDto {
    
    @PositiveOrZero(message = "Valor total deve ser maior ou igual a zero")
    private String valorTotal;
    
    @PositiveOrZero(message = "Desconto deve ser maior ou igual a zero")
    private String desconto;
    
    private LocalDate dataVenda;
    
    private List<UUID> pedidosIds;
}
