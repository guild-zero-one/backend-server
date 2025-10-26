package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarTokenUseCase {
    
    private final TokenRepositoryPort tokenRepositoryPort;
    
    public boolean executar(String token, UserDetails userDetails) {
        return tokenRepositoryPort.validarToken(token);
    }
    
    public boolean isTokenExpirado(String token) {
        return !tokenRepositoryPort.validarToken(token);
    }
    
    public String extrairUsername(String token) {
        // Implementar conforme necessário ou remover se não usado
        return "username";
    }
}
