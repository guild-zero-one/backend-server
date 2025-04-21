package com.zeroone.simlady.dto.produtoImagem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProdutoImagemResponseDto {
    @Schema(description = "ID da Imagem", example = "1")
    private Integer id;

    @Schema(description = "URL da imagem", example = "https://boticario-malbec.png")
    private String urlImagem;

    @Schema(description = "Imagem vai ser exibida no site", example = "true")
    private Boolean imagemPrincipal;

    @Schema(description = "ID do Produto", example = "1")
    private Integer produtoId;
}
