package com.zeroone.simlady.dto.pedidoItem;

import com.zeroone.simlady.dto.produto.ProdutoResumoVendaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PedidoItemComProdutoDto {
    @Schema(description = "ID do Item", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço Unitário item no momento da compra", example = "100.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Valor de venda do item", example = "129.90")
    private BigDecimal valorVenda;

    @Schema(description = "Dados do produto")
    private ProdutoResumoVendaDto produto;
}

