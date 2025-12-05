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

    @Value("${jwt.cookie.name:jwt-token}")
    private String cookieName;

    @Value("${jwt.cookie.max-age:86400}") // 24 horas em segundos
    private int cookieMaxAge;

    @Value("${jwt.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${jwt.cookie.http-only:true}")
    private boolean cookieHttpOnly;

    @Value("${jwt.cookie.same-site:Strict}")
    private String cookieSameSite;

    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        String token = tokenRepositoryPort.gerarToken(usuario);

        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setHttpOnly(cookieHttpOnly);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");

        response.addHeader("Set-Cookie", 
            String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=%s%s",
                cookieName, token, cookieMaxAge, cookieSameSite,
                cookieSecure ? "; Secure" : ""));

        return token;
    }
}
