package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter {
    
    private final TokenRepositoryPort tokenRepositoryPort;
    
    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        String token = tokenRepositoryPort.gerarToken(usuario);
        
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")  
                .maxAge(jwtTokenValidity)
                .build();
        
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        return token;
    }
}
