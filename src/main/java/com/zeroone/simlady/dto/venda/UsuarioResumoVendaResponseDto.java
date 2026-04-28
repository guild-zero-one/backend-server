package com.zeroone.simlady.dto.venda;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResumoVendaResponseDto {
    @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "Maria")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Oliveira")
    private String sobrenome;

    @Schema(description = "Imagem de perfil do usuário", example = "https://example.com/avatar.png")
    private String urlImagem;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;
}
