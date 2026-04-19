package com.zeroone.simlady.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProdutoLoteResponseDto {
    @Schema(description = "Total de produtos processados")
    private Integer totalProcessado;

    @Schema(description = "Total de produtos criados com sucesso")
    private Integer totalCriado;

    @Schema(description = "Total de produtos com erro")
    private Integer totalErro;

    @Schema(description = "Lista de produtos criados com sucesso")
    private List<ProdutoResponseDto> produtosCriados;

    @Schema(description = "Lista de produtos com erro")
    private List<ProdutoErroResponseDto> produtosErro;

    @Data
    @AllArgsConstructor
    public static class ProdutoErroResponseDto {
        @Schema(description = "Nome do produto")
        private String nomeProduto;

        @Schema(description = "Mensagem de erro")
        private String erro;
    }
}

