package com.zeroone.simlady.dto.loteItem;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoteItemResponseDto {
    private Integer id;
    private Double qtdLoteCompra;
    private Double valorUnitarioCompra;
    private String dataValidade;
    private Integer produtoId;
    private Integer loteId;
}
