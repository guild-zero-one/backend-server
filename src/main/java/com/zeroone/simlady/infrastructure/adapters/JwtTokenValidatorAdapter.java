package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.ValidadorTokenJwtPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenValidatorAdapter implements ValidadorTokenJwtPort {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public boolean validarToken(String token, UserDetails userDetails) {
        try {
            String username = extrairUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpirado(token);
        } catch (Exception e) {
            log.warn("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenExpirado(String token) {
        try {
            Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
            return expirationDate.before(new Date(System.currentTimeMillis()));
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            log.warn("Erro ao verificar expiração do token: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public String extrairUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
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
