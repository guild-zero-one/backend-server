package com.zeroone.simlady.core.adapters.dtos.usuario;

import com.zeroone.simlady.core.domain.usuario.Permissao;

public record UsuarioCreateRequestDto(
        String nome,
        String sobrenome,
        String email,
        String senha,
        String celular,
        Permissao permissao
) {}
