package com.zeroone.simlady.dto.loteItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoteItemResponseDto {
    @Schema(description = "ID do Item", example = "1")
    private Integer id;

    @Schema(description = "Quantidade de itens", example = "4")
    private Double qtdLoteCompra;

    @Schema(description = "Valor unitário do item", example = "20")
    private Double valorUnitarioCompra;

    @Schema(description = "Data de validade", example = "20/05/2027")
    private String dataValidade;

    @Schema(description = "ID do Produto", example = "1")
    private Integer produtoId;

    @Schema(description = "ID do Lote", example = "1")
    private Integer loteId;
}
