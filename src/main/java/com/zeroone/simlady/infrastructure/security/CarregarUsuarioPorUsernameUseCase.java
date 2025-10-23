package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.core.application.ports.AutenticacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarregarUsuarioPorUsernameUseCase {
    
    private final AutenticacaoRepositoryPort autenticacaoRepositoryPort;
    
    public UserDetails executar(String username) {
        return autenticacaoRepositoryPort.carregarUsuarioPorUsername(username);
    }
}
