package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.core.application.ports.AutenticacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticarUsuarioSecurityUseCase {
    
    private final AutenticacaoRepositoryPort autenticacaoRepositoryPort;
    
    public Authentication executar(String username, String password) {
        return autenticacaoRepositoryPort.autenticar(username, password);
    }
}
