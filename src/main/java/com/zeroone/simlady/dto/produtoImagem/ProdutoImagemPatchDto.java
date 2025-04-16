package com.zeroone.simlady.dto.produtoImagem;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoImagemPatchDto {
    @NotBlank(message = "URL da imagem não pode ser nula ou vazia")
    private String urlImagem;
    @NotBlank(message = "Imagem principal não pode ser nula ou vazia")
    private Boolean imagemPrincipal;
    @NotBlank(message = "ID do produto não pode ser nulo ou vazio")
    private Integer produtoId;
}