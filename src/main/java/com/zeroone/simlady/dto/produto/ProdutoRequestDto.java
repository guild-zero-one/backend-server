package com.zeroone.simlady.dto.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDto {
    @NotBlank(message = "Nome não deve ser vazio")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    private String nome;
    @NotBlank(message = "Nome Fantasia não deve ser vazio")
    @Size(min = 3, max = 70, message = "O SKU deve ter entre 3 e 50 caracteres")
    private String sku;
    @NotBlank(message = "Descrição não deve ser vazio")
    @Size(min = 3, max = 255, message = "A descrição deve ter entre 3 e 255 caracteres")
    private String desc;
    @NotBlank(message = "Tag não deve ser vazio")
    @Size(min = 3, max = 50, message = "A tag deve ter entre 3 e 50 caracteres")
    private String tag;
    @NotBlank(message = "Quantidade não deve ser vazio")
    private Integer quantidade;
    @NotBlank(message = "Preço unitário não deve ser vazio")
    private Double precoUnitario;
    @NotBlank(message = "Catalogo não deve ser vazio")
    private Boolean catalogo;
    @NotBlank(message = "Valor de venda não deve ser vazio")
    private Double valorVenda;
    @NotBlank(message = "Fornecedor não deve ser vazio")
    private Integer fornecedorId;
}
