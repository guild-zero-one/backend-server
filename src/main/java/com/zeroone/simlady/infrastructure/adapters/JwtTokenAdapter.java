package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter {
    
    private final TokenRepositoryPort tokenRepositoryPort;
    
    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        String token = tokenRepositoryPort.gerarToken(usuario);
        
        // Adiciona o token como cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtTokenValidity);
        response.addCookie(cookie);
        
        return token;
    }
}
