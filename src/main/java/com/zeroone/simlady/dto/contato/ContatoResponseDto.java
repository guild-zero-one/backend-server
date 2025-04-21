package com.zeroone.simlady.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class ContatoResponseDto {
    @Schema(description = "ID do Contato", example = "1")
    private Integer id;

    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;

    @Schema(description = "ID do Usuário", example = "1")
    private Integer fkUsuario;

}

