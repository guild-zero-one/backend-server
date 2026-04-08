package com.zeroone.simlady.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class ProdutoRequestDto {
    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    @Schema(description = "Nome do produto", example = "Malbec Perfume")
    private String nome;

    @Size(min = 3, max = 70, message = "O SKU deve ter entre 3 e 50 caracteres")
    @Schema(description = "SKU do produto", example = "MAL-PER-100")
    private String sku;

    @NotEmpty(message = "Descrição não pode ser vazia, apenas.")
    @Size(min = 3, max = 255, message = "A descrição deve ter entre 3 e 255 caracteres")
    @Schema(description = "Descrição do produto", example = "O melhor perfume do Brasil")
    private String descricao;

    @NotNull(message = "Quantidade não pode ser nula.")
    @Positive(message = "Quantidade deve ser um número positivo.")
    @Schema(description = "Quantidade de produtos", example = "12")
    private Integer quantidade;

    @NotBlank(message = "URL da imagem não pode ser vazia.")
    @Schema(description = "URL da imagem do produto", example = "https://boticario-malbec.png")
    private String urlImagem;

    @NotNull(message = "Preço Unitário não pode ser nulo.")
    @Positive(message = "Preço unitário deve ser um número positivo.")
    @Schema(description = "Preço unitário de compra", example = "30.00")
    private Double precoUnitario;

    @NotNull(message = "Catálogo não pode ser nulo.")
    @Schema(description = "Exibição do produto no site", example = "true")
    private Boolean catalogo;

    @NotNull(message = "Valor da Venda não pode ser nulo.")
    @Positive(message = "Valor da venda deve ser um número positivo.")
    @Schema(description = "Valor de venda", example = "50.00")
    private Double valorVenda;

    @NotNull(message = "ID do Fornecedor não pode ser nulo.")
    @Schema(description = "ID do fornecedor/marca", example = "1")
    private UUID fornecedorId;
}
