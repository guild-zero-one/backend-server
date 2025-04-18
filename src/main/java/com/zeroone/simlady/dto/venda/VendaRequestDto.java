package com.zeroone.simlady.dto.venda;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VendaRequestDto {


    @NotNull
    private Double desconto;

    @NotNull
    private Boolean pagamentoRealizado;

    @NotNull
    @NotEmpty
    private List<Integer> pedidos;
}
