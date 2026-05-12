package com.zeroone.simlady.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record StorageUploadResponseDto(
        @Schema(description = "Mensagem de confirmação", example = "Imagem enviada com sucesso.")
        String mensagem,

        @Schema(description = "URL pública da imagem", example = "http://localhost:8080/uploads/produtos/produto-42.jpg")
        String url,

        @Schema(description = "Pasta da entidade", example = "produtos")
        String folder,

        @Schema(description = "ID da entidade")
        UUID entityId
) {}
