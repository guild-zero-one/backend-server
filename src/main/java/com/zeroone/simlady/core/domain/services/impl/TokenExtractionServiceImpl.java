package com.zeroone.simlady.core.domain.services.impl;

import com.zeroone.simlady.core.domain.services.TokenExtractionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Implementação do serviço de extração de token JWT
 */
@Service
@Slf4j
public class TokenExtractionServiceImpl implements TokenExtractionService {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public UUID extrairIdUsuario(HttpServletRequest request) {
        try {
            String token = recuperarToken(request);
            if (token == null) {
                throw new IllegalArgumentException("Token não encontrado na requisição");
            }
            
            String usuarioId = extrairSubject(token);
            return UUID.fromString(usuarioId);
        } catch (Exception e) {
            log.error("Erro ao extrair ID do usuário do token: {}", e.getMessage());
            throw new IllegalArgumentException("Token inválido ou expirado", e);
        }
    }

    @Override
    public String recuperarToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String extrairSubject(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            throw new IllegalArgumentException("Token expirado", e);
        } catch (Exception e) {
            log.error("Erro ao extrair subject do token: {}", e.getMessage());
            throw new IllegalArgumentException("Token inválido", e);
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(parseSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey parseSecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}
