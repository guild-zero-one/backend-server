package com.zeroone.simlady.core.adapters.dtos.usuario;

public record UsuarioLoginRequestDto(
        String email,
        String senha
) {}
