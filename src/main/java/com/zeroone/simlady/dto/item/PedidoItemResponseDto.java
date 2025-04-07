package com.zeroone.simlady.dto.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoItemResponseDto {
    private Integer id;
    private Integer idProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}