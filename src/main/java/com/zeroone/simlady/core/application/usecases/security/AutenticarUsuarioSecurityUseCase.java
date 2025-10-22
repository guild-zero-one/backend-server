package com.zeroone.simlady.core.application.usecases.security;

import com.zeroone.simlady.core.application.ports.AutenticacaoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticarUsuarioSecurityUseCase {
    
    private final AutenticacaoPort autenticacaoPort;
    
    public Authentication executar(String username, String password) {
        return autenticacaoPort.autenticar(username, password);
    }
}