package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExtrairIdUsuarioUseCase {

    private final TokenRepositoryPort tokenRepositoryPort;

    public UUID executar(String token) {
        return tokenRepositoryPort.extrairIdUsuario(token);
    }
}
