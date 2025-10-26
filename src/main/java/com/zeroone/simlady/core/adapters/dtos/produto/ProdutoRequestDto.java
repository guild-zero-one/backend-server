package com.zeroone.simlady.core.adapters.dtos.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProdutoRequestDto {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "SKU é obrigatório")
    private String sku;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    private String tag;
    
    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade deve ser maior ou igual a zero")
    private Integer quantidade;
    
    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço unitário deve ser maior que zero")
    private Double precoUnitario;
    
    private Boolean catalogo;
    
    @NotNull(message = "Valor de venda é obrigatório")
    @Positive(message = "Valor de venda deve ser maior que zero")
    private Double valorVenda;
    
    private String imagemUrl;
    
    private UUID idMarca;
}
