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
        
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        log.debug("Processando requisição: {} {}", method, requestPath);
        
        // Pular validação JWT para endpoints públicos
        if (devePularValidacaoJWT(requestPath, method)) {
            log.debug("Pulando validação JWT para endpoint público: {} {}", method, requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        // Permitir que o Spring Security trate URLs inexistentes
        // Se a requisição não tem token, deixar o Spring Security decidir
        String token = extrairTokenDoRequest(request);
        if (token == null || token.trim().isEmpty()) {
            log.debug("Token não encontrado, deixando Spring Security tratar: {} {}", method, requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Validar token
            if (!tokenValidator.validarToken(token, null)) {
                log.warn("Token JWT inválido ou expirado para: {} {}", method, requestPath);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token de acesso inválido ou expirado\"}");
                return;
            }
            
            // Extrair email do token
            String email = tokenExtractor.extrairEmailDoToken(token);
            
            if (email == null || email.trim().isEmpty()) {
                log.warn("Email não encontrado no token para: {} {}", method, requestPath);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token de acesso inválido\"}");
                return;
            }
            
            // Carregar detalhes do usuário
            UserDetails userDetails = autenticacaoAdapter.carregarUsuarioPorUsername(email);
            
            if (userDetails == null) {
                log.warn("Usuário não encontrado para email: {} em {}", email, requestPath);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Usuário não encontrado\"}");
                return;
            }
            
            // Verificar se o usuário está ativo
            if (!userDetails.isEnabled()) {
                log.warn("Usuário desabilitado: {} em {}", email, requestPath);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Usuário desabilitado\"}");
                return;
            }
            
            // Configurar autenticação no contexto de segurança
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                log.debug("Usuário autenticado com sucesso: {} para {}", email, requestPath);
            }
            
        } catch (Exception e) {
            log.error("Erro ao processar autenticação JWT para {} {}: {}", method, requestPath, e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erro interno do servidor\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extrairTokenDoRequest(HttpServletRequest request) {
        // Primeiro, tentar extrair do header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("Token extraído do header Authorization");
            return token;
        }
        
        // Fallback: extrair dos cookies
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    log.debug("Token extraído do cookie");
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    private boolean devePularValidacaoJWT(String requestPath, String method) {
        // Paths específicos que devem pular a validação JWT
        String[] pathsPublicos = {
            "/usuarios/login", 
            "/usuarios/logout",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-resources",
            "/v3/api-docs",
            "/webjars",
            "/actuator",
            "/h2-console",
            "/error"
        };
        
        // Verificar paths específicos
        for (String pathPublico : pathsPublicos) {
            if (requestPath.equals(pathPublico) || requestPath.startsWith(pathPublico + "/")) {
                return true;
            }
        }
        
        // Verificar se é POST para /usuarios (cadastro)
        if (requestPath.equals("/usuarios") && "POST".equals(method)) {
            return true;
        }
        
        return false;
    }
}

