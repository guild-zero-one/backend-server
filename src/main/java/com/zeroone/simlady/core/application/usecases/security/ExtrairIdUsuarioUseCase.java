package com.zeroone.simlady.core.application.usecases.security;

import com.zeroone.simlady.core.domain.services.TokenExtractionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Use case responsável por extrair o ID do usuário a partir do token JWT
 */
@Component
@RequiredArgsConstructor
public class ExtrairIdUsuarioUseCase {

    private final TokenExtractionService tokenExtractionService;

    /**
     * Executa a extração do ID do usuário a partir do token JWT presente na requisição
     * 
     * @param request requisição HTTP contendo o token
     * @return UUID do usuário extraído do token
     * @throws IllegalArgumentException se o token for inválido ou não contiver um ID válido
     */
    public UUID executar(HttpServletRequest request) {
        return tokenExtractionService.extrairIdUsuario(request);
    }
}
