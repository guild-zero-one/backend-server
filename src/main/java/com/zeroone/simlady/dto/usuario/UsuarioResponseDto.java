package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResponseDto {
    @Schema(description = "ID do Usuário", example = "1")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Imagem de Perfil", example = "blob.url.com")
    private String urlImagem;
}
