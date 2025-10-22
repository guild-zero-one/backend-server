package com.zeroone.simlady.core.application.usecases.security;

import com.zeroone.simlady.core.application.ports.AutenticacaoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarregarUsuarioPorUsernameUseCase {
    
    private final AutenticacaoPort autenticacaoPort;
    
    public UserDetails executar(String username) {
        return autenticacaoPort.carregarUsuarioPorUsername(username);
    }
}
