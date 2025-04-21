package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioTokenDto {
    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Schema(description = "Token de Autenticação")
    private String token;
}
