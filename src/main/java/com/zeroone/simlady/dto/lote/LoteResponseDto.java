package com.zeroone.simlady.dto.lote;

import lombok.Data;
import lombok.*;

@Data
@Getter
@Setter
public class LoteResponseDto {
    private Integer id;
    private Integer qtdLote;
    private Double valorTotal;
}
