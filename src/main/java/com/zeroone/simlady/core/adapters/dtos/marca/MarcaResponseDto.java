package com.zeroone.simlady.core.adapters.dtos.marca;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarcaResponseDto(
        UUID id,
        String nome,
        String descricao,
        String imagemUrl,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
