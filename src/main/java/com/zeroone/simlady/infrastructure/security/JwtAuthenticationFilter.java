package com.zeroone.simlady.infrastructure.security;

import com.zeroone.simlady.infrastructure.adapters.JwtTokenExtractorAdapter;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenValidatorAdapter;
import com.zeroone.simlady.infrastructure.adapters.AutenticacaoRepositoryAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenExtractorAdapter tokenExtractor;
    private final JwtTokenValidatorAdapter tokenValidator;
    private final AutenticacaoRepositoryAdapter autenticacaoAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Extrair token da requisição
            String token = extrairTokenDoRequest(request);
            
            if (token != null && tokenValidator.validarToken(token, null)) {
                // Extrair email do usuário do token
                String email = extrairEmailDoToken(token);
                
                // Carregar detalhes do usuário usando o email
                UserDetails userDetails = autenticacaoAdapter.carregarUsuarioPorUsername(email);
                
                if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Usuário autenticado via JWT: {}", userDetails.getUsername());
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao processar token JWT: {}", e.getMessage());
            // Não bloquear a requisição, apenas logar o erro
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extrairTokenDoRequest(HttpServletRequest request) {
        // Extrair token dos cookies
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
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
    
    private String extrairEmailDoToken(String token) {
        try {
            // Usar o TokenRepositoryAdapter para extrair o email do token
            // Como não temos método direto, vamos usar reflexão ou criar um método no adapter
            return tokenExtractor.extrairEmailDoToken(token);
        } catch (Exception e) {
            log.warn("Erro ao extrair email do token: {}", e.getMessage());
            return null;
        }
    }
}
