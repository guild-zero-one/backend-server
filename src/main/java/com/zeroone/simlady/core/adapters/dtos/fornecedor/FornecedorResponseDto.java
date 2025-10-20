package com.zeroone.simlady.core.adapters.dtos.fornecedor;

import java.time.LocalDateTime;
import java.util.UUID;

public record FornecedorResponseDto(
        UUID id,
        String nome,
        String descricao,
        String cnpj,
        String imagemUrl,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
