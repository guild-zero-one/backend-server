package com.zeroone.simlady.dto.lote;

import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class LoteRequestDto {
    @NotNull(message = "Quantidade total não pode ser nulo.")
    @Positive(message = "Quantidade total deve ser um número positivo.")
    @Schema(description = "Quantidade total de produtos", example = "12")
    private Integer qtdLote;

    @NotNull(message = "Valor total não pode ser nulo.")
    @Positive(message = "Valor total deve ser um número positivo.")
    @Schema(description = "Valor total de produtos", example = "120.00")
    private Double valorTotal;

    @NotBlank(message = "Um lote deve obrigatóriamente conter itens.")
    @ArraySchema(
            schema = @Schema(implementation = LoteItemRequestDto.class),
            arraySchema = @Schema(description = "Itens no lote")
    )
    private List<LoteItemRequestDto> loteItems;
}
