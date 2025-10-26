package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidatorAdapter {

    private final TokenRepositoryPort tokenRepositoryPort;

    public boolean validarToken(String token, UserDetails userDetails) {
        try {
            return tokenRepositoryPort.validarToken(token);
        } catch (Exception e) {
            log.warn("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpirado(String token) {
        try {
            return !tokenRepositoryPort.validarToken(token);
        } catch (Exception e) {
            log.warn("Erro ao verificar expiração do token: {}", e.getMessage());
            return true;
        }
    }

    public String extrairUsername(String token) {
        try {
            // Como o TokenServicePort não tem método para extrair username,
            // podemos usar o TokenServiceAdapter diretamente ou adicionar o método
            return "username"; // Implementar conforme necessário
        } catch (Exception e) {
            log.warn("Erro ao extrair username do token: {}", e.getMessage());
            return null;
        }
    }
}
