package com.zeroone.simlady.core.domain.services;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

/**
 * Serviço de domínio responsável pela extração de informações do token JWT
 */
public interface TokenExtractionService {
    
    /**
     * Extrai o ID do usuário a partir do token JWT presente na requisição
     * 
     * @param request requisição HTTP contendo o token
     * @return UUID do usuário extraído do token
     * @throws IllegalArgumentException se o token for inválido ou não contiver um ID válido
     */
    UUID extrairIdUsuario(HttpServletRequest request);
    
    /**
     * Recupera o token JWT da requisição HTTP
     * 
     * @param request requisição HTTP
     * @return token JWT como string, ou null se não encontrado
     */
    String recuperarToken(HttpServletRequest request);
    
    /**
     * Extrai o subject (ID do usuário) de um token JWT
     * 
     * @param token token JWT
     * @return ID do usuário como string
     * @throws IllegalArgumentException se o token for inválido
     */
    String extrairSubject(String token);
}
