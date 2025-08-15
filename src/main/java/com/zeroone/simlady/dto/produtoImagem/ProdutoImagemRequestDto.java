package com.zeroone.simlady.dto.produtoImagem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdutoImagemRequestDto {
//    @Schema(description = "URL da imagem", example = "https://boticario-malbec.png")
//    private String urlImagem;

    @NotNull(message = "Imagem principal não pode ser nula.")
    @Schema(description = "Imagem vai ser exibida no site", example = "true")
    private Boolean imagemPrincipal;

    @NotNull(message = "ID do Produto não pode ser nulo.")
    @Schema(description = "ID do Produto", example = "1")
    private Integer produtoId;
}
