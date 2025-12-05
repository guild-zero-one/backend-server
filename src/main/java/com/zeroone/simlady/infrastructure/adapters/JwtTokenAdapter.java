package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter {
    
    private final TokenRepositoryPort tokenRepositoryPort;

    @Value("${jwt.cookie.name:jwt-token}")
    private String cookieName;

    @Value("${jwt.cookie.max-age:86400}") 
    private int cookieMaxAge;

    @Value("${jwt.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${jwt.cookie.http-only:true}")
    private boolean cookieHttpOnly;

    @Value("${jwt.cookie.same-site:Strict}")
    private String cookieSameSite;

    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        String token = tokenRepositoryPort.gerarToken(usuario);
    
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(cookieSameSite)
                .build();
    
        response.addHeader("Set-Cookie", cookie.toString());
    
        return token;
    }
}
