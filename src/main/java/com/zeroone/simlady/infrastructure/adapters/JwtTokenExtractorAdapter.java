package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractorAdapter {
    
    private final TokenRepositoryPort tokenRepositoryPort;

    public UUID extrairIdUsuario(HttpServletRequest request) {
        String token = extrairTokenDoRequest(request);
        if (token == null) {
            throw new IllegalArgumentException("Token não encontrado na requisição");
        }
        return tokenRepositoryPort.extrairIdUsuario(token);
    }
    
    public String extrairEmailDoToken(String token) {
        return tokenRepositoryPort.extrairEmailDoToken(token);
    }
    
    private String extrairTokenDoRequest(HttpServletRequest request) {
        // Extrair token dos cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        // Fallback: extrair do header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }
}
