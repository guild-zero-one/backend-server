package com.zeroone.simlady.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResumoPedidoResponseDto {
    @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "Maria")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Oliveira")
    private String sobrenome;

    @Schema(description = "Imagem de perfil do usuário", example = "https://example.com/avatar.png")
    private String urlImagem;

    @Schema(description = "Celular do usuário", example = "11988887777")
    private String celular;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;
}
