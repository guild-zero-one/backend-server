package com.zeroone.simlady.dto.lote;

import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class LoteResponseItemDto {
    @Schema(description = "ID do Lote", example = "1")
    private Integer id;

    @Schema(description = "Quantidade total de produtos", example = "12")
    private Integer qtdLote;

    @Schema(description = "Valor total de produtos", example = "120.00")
    private Double valorTotal;

    @Schema(description = "Itens no lote", implementation = LoteItemResponseDto.class)
    private List<LoteItemResponseDto> loteItems;
}

