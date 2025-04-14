package com.zeroone.simlady.dto.lote;

import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class LoteResponseItemDto {
    private Integer id;
    private Integer qtdLote;
    private Double valorTotal;
    private List<LoteItemResponseDto> loteItems;
}

