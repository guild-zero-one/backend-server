package com.zeroone.simlady.core.adapters.dtos.usuario;

import com.zeroone.simlady.core.domain.usuario.Permissao;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioTokenResponseDto(
        UUID id,
        String nome,
        String sobrenome,
        String email,
        String celular,
        Boolean ativo,
        Permissao permissao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        String token
) {}
