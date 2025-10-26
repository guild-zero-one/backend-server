package com.zeroone.simlady.core.adapters.dtos.usuario;

public record UsuarioUpdateRequestDto(
        String nome,
        String sobrenome,
        String email,
        String celular
) {}
