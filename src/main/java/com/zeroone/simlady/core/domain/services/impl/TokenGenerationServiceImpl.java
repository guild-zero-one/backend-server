package com.zeroone.simlady.core.domain.services.impl;

import com.zeroone.simlady.core.domain.services.TokenGenerationService;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Implementação do serviço de geração de token JWT
 */
@Service
@Slf4j
public class TokenGenerationServiceImpl implements TokenGenerationService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    @Override
    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        try {
            String token = Jwts.builder()
                    .setSubject(usuario.getId().toString())
                    .claim("email", usuario.getEmail())
                    .signWith(parseSecret())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000))
                    .compact();

            // Adiciona o token como cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtTokenValidity));
            response.addCookie(cookie);

            log.debug("Token JWT gerado para usuário: {}", usuario.getId());
            return token;
        } catch (Exception e) {
            log.error("Erro ao gerar token JWT para usuário {}: {}", usuario.getId(), e.getMessage());
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    private SecretKey parseSecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}
