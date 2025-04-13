package com.zeroone.simlady.dto.venda;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VendaResponseDto {

    private Integer id;

    private BigDecimal valorTotal;

    private BigDecimal desconto;

    private Boolean pagamentoRealizado;

    private LocalDate dataVenda;
}
