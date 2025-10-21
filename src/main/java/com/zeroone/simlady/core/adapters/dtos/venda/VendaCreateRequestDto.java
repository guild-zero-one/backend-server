package com.zeroone.simlady.core.adapters.dtos.venda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VendaCreateRequestDto {
    
    @NotBlank(message = "Valor total é obrigatório")
    @PositiveOrZero(message = "Valor total deve ser maior ou igual a zero")
    private String valorTotal;
    
    @NotBlank(message = "Desconto é obrigatório")
    @PositiveOrZero(message = "Desconto deve ser maior ou igual a zero")
    private String desconto;
    
    @NotNull(message = "Data da venda é obrigatória")
    private LocalDate dataVenda;
    
    private List<UUID> pedidosIds;
}
