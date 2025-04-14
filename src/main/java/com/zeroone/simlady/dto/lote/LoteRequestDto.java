package com.zeroone.simlady.dto.lote;

import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter

public class LoteRequestDto {
    @NotBlank(message = "O campo 'qtdLote' é obrigatório")
    private Integer qtdLote;
    @NotBlank(message = "O campo 'valorTotal' é obrigatório")
    private Double valorTotal;
    @NotBlank(message = "O campo 'loteItems' é obrigatório")
    private List<LoteItemRequestDto> loteItems;
}
