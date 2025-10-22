package com.zeroone.simlady.core.domain.services;

import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Serviço de domínio responsável pela geração de tokens JWT
 */
public interface TokenGenerationService {
    
    /**
     * Gera um token JWT para o usuário especificado
     * 
     * @param usuario usuário para o qual o token será gerado
     * @param response resposta HTTP onde o token será adicionado como cookie
     * @return token JWT gerado
     */
    String gerarToken(Usuario usuario, HttpServletResponse response);
}
