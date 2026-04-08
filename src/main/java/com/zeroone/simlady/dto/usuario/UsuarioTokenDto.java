package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioTokenDto {
    @Schema(description = "Token de Autenticação")
    private String token;

    @Schema(description = "Dados do usuário autenticado")
    private UsuarioResponseDto usuario;
}
