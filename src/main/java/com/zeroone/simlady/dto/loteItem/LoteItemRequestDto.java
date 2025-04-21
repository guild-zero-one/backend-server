package com.zeroone.simlady.dto.loteItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class LoteItemRequestDto {
    @NotNull(message = "Quantidade de itens não pode ser nulo.")
    @Positive(message = "Quantidade de itens deve ser um número positivo.")
    @Schema(description = "Quantidade de itens", example = "4")
    private Double qtdLoteCompra;

    @NotNull(message = "Valor unitário  não pode ser nulo.")
    @Positive(message = "Valor unitário deve ser um número positivo.")
    @Schema(description = "Valor unitário do item", example = "20")
    private Double valorUnitarioCompra;

    @NotNull(message = "Data de validade não pode ser nulo.")
    @Future(message = "Data de validade deve estar no futuro.")
    @Schema(description = "Data de validade", example = "20/05/2027")
    private LocalDate dataValidade;

    @NotNull(message = "ID do Produto não pode ser nulo.")
    @Schema(description = "ID do Produto", example = "1")
    private Integer produtoId;

    @NotNull(message = "ID do Lote não pode ser nulo.")
    @Schema(description = "ID do Lote", example = "1")
    private Integer loteId;
}
