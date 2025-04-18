package com.zeroone.simlady.dto.loteItem;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class LoteItemRequestDto {
    @NotBlank(message = "O campo 'qtdLoteCompra' é obrigatório")
    private Double qtdLoteCompra;
    @NotBlank(message = "O campo 'valorUnitarioCompra' é obrigatório")
    private Double valorUnitarioCompra;
    @NotBlank(message = "O campo 'dataValidade' é obrigatório")
    private LocalDate dataValidade;
    @NotBlank(message = "O campo 'produtoId' é obrigatório")
    private Integer produtoId;
    private Integer loteId;
}
