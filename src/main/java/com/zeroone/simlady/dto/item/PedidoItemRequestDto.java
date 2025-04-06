package com.zeroone.simlady.dto.item;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoItemRequestDto {

    private Integer idLoteProduto;

    @Positive
    private Integer quantidade;

    @Positive
    private BigDecimal precoUnitario;
}
