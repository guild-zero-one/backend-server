package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    @Override
    public String gerarToken(Usuario usuario) {
        try {
            return Jwts.builder()
                    .setSubject(usuario.getId().toString())
                    .claim("email", usuario.getEmail())
                    .signWith(parseSecret())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000))
                    .compact();
        } catch (Exception e) {
            log.error("Erro ao gerar token JWT para usuário {}: {}", usuario.getId(), e.getMessage());
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    @Override
    public UUID extrairIdUsuario(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String usuarioId = claims.getSubject();
            return UUID.fromString(usuarioId);
        } catch (Exception e) {
            log.error("Erro ao extrair ID do usuário do token: {}", e.getMessage());
            throw new IllegalArgumentException("Token inválido ou expirado", e);
        }
    }

    @Override
    public boolean validarToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            return false;
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
