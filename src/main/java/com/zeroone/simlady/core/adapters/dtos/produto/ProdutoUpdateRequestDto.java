package com.zeroone.simlady.core.adapters.dtos.produto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProdutoUpdateRequestDto {
    
    private String nome;
    
    private String sku;
    
    private String descricao;
    
    private String tag;
    
    @PositiveOrZero(message = "Quantidade deve ser maior ou igual a zero")
    private Integer quantidade;
    
    @Positive(message = "Preço unitário deve ser maior que zero")
    private Double precoUnitario;
    
    private Boolean catalogo;
    
    @Positive(message = "Valor de venda deve ser maior que zero")
    private Double valorVenda;
    
    private String imagemUrl;
    private UUID idMarca;
}
