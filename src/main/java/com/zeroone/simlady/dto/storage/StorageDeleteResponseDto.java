package com.zeroone.simlady.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record StorageDeleteResponseDto(
        @Schema(description = "Mensagem de confirmação", example = "Imagem removida com sucesso.")
        String mensagem,

        @Schema(description = "Pasta da entidade", example = "produtos")
        String folder,

        @Schema(description = "ID da entidade")
        UUID entityId
) {}
