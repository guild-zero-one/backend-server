package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResumoVendaDto {
    @Schema(description = "ID do Usuário", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "Maria")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Oliveira")
    private String sobrenome;

    @Schema(description = "URL da imagem do usuário", example = "https://...")
    private String urlImagem;

    @Schema(description = "Número de celular", example = "(11) 98888-7777")
    private String celular;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;
}

