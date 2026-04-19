package com.zeroone.simlady.config.security.sso;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ClerkJwtFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClerkJwtFilter.class);

    private final JWKSource<SecurityContext> jwkSource;

    public ClerkJwtFilter(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().equals("/usuarios/sso");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            LOGGER.info("[CLERK SSO] - Header Authorization ausente ou inválido");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Clerk ausente");
            return;
        }

        try {
            String token = header.substring(7);
            SignedJWT jwt = SignedJWT.parse(token);

            DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
            processor.setJWSKeySelector(
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource)
            );

            JWTClaimsSet claims = processor.process(jwt, null);
            String clerkId = claims.getSubject(); // "sub" = clerkId

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(clerkId, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(auth);

            LOGGER.info("[CLERK SSO] - clerkId autenticado: {}", clerkId);

        } catch (Exception e) {
            LOGGER.warn("[CLERK SSO] - Token Clerk inválido: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Clerk inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}