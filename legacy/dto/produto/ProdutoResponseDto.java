package com.zeroone.simlady.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class ProdutoResponseDto {
    @Schema(description = "ID do Produto", example = "1")
    private Integer id;

    @Schema(description = "Nome do produto", example = "Malbec Perfume")
    private String nome;

    @Schema(description = "SKU do produto", example = "MAL-PER-100")
    private String sku;

    @Schema(description = "Descrição do produto", example = "O melhor perfume do Brasil")
    private String descricao;

    @Schema(description = "TAG do produto", example = "Lançamento")
    private String tag;

    @Schema(description = "Quantidade de produtos", example = "12")
    private Integer quantidade;

    @Schema(description = "Preço unitário de compra", example = "30.00")
    private Double precoUnitario;

    @Schema(description = "Exibição do produto no site", example = "true")
    private Boolean catalogo;

    @Schema(description = "Valor de venda", example = "50.00")
    private Double valorVenda;

    @Schema(description = "ID do fornecedor/marca", example = "1")
    private Integer fornecedorId;
}
