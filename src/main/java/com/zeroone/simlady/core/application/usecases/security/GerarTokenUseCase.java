package com.zeroone.simlady.core.application.usecases.security;

import com.zeroone.simlady.core.domain.services.TokenGenerationService;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case responsável por gerar tokens JWT para usuários
 */
@Component
@RequiredArgsConstructor
public class GerarTokenUseCase {

    private final TokenGenerationService tokenGenerationService;

    /**
     * Executa a geração de um token JWT para o usuário especificado
     * 
     * @param usuario usuário para o qual o token será gerado
     * @param response resposta HTTP onde o token será adicionado como cookie
     * @return token JWT gerado
     */
    public String executar(Usuario usuario, HttpServletResponse response) {
        return tokenGenerationService.gerarToken(usuario, response);
    }
}
