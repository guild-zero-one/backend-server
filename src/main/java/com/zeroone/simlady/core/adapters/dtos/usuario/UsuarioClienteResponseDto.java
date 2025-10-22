package com.zeroone.simlady.core.adapters.dtos.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioClienteResponseDto(
        UUID id,
        String nome,
        String sobrenome,
        String email,
        String celular,
        LocalDateTime criadoEm
) {}
