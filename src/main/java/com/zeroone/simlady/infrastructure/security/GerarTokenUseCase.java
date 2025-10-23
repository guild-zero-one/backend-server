package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GerarTokenUseCase {

    private final TokenRepositoryPort tokenRepositoryPort;

    public String executar(Usuario usuario) {
        return tokenRepositoryPort.gerarToken(usuario);
    }
}
