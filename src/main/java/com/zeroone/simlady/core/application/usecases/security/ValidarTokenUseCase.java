package com.zeroone.simlady.core.application.usecases.security;

import com.zeroone.simlady.core.application.ports.ValidadorTokenJwtPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarTokenUseCase {
    
    private final ValidadorTokenJwtPort validadorTokenJwtPort;
    
    public boolean executar(String token, UserDetails userDetails) {
        return validadorTokenJwtPort.validarToken(token, userDetails);
    }
    
    public boolean isTokenExpirado(String token) {
        return validadorTokenJwtPort.isTokenExpirado(token);
    }
    
    public String extrairUsername(String token) {
        return validadorTokenJwtPort.extrairUsername(token);
    }
}
