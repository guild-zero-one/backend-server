package com.zeroone.simlady.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Só define status de autenticação se não houver outro status já definido
        if (response.getStatus() == HttpServletResponse.SC_OK || response.getStatus() == 0) {
            if (authException.getClass().equals(BadCredentialsException.class) || authException.getClass().equals(InsufficientAuthenticationException.class)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de acesso inválido ou expirado");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
            }
        }
        // Se já há um status definido (404, 400, 500, etc.), não interfere
    }
}