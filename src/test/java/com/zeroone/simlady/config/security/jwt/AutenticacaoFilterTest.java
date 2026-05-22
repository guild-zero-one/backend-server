package com.zeroone.simlady.config.security.jwt;

import com.zeroone.simlady.service.AutenticacaoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoFilterTest {

    @Mock
    private AutenticacaoService autenticacaoService;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    private AutenticacaoFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AutenticacaoFilter(autenticacaoService, gerenciadorTokenJwt);
    }

    @Test
    @DisplayName("Deve retornar status 401 quando token estiver expirado")
    void deveRetornar401QuandoTokenEstiverExpirado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.expirado.aqui");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(claims.getSubject()).thenReturn("usuario@teste.com");
        doThrow(new ExpiredJwtException(null, claims, "Token expirado"))
                .when(gerenciadorTokenJwt).getUsernameFromToken("token.expirado.aqui");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("Deve escrever body JSON com mensagem de token expirado")
    void deveEscreverBodyJsonComMensagemTokenExpirado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.expirado.aqui");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(claims.getSubject()).thenReturn("usuario@teste.com");
        doThrow(new ExpiredJwtException(null, claims, "Token expirado"))
                .when(gerenciadorTokenJwt).getUsernameFromToken("token.expirado.aqui");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getContentAsString()).contains("message");
        assertThat(response.getContentAsString()).contains("Token expirado.");
    }

    @Test
    @DisplayName("Deve interromper o filter chain quando token estiver expirado")
    void deveInterromperFilterChainQuandoTokenExpirado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.expirado.aqui");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(claims.getSubject()).thenReturn("usuario@teste.com");
        doThrow(new ExpiredJwtException(null, claims, "Token expirado"))
                .when(gerenciadorTokenJwt).getUsernameFromToken("token.expirado.aqui");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("Deve definir Content-Type application/json quando token estiver expirado")
    void deveDefinirContentTypeJsonQuandoTokenExpirado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.expirado.aqui");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(claims.getSubject()).thenReturn("usuario@teste.com");
        doThrow(new ExpiredJwtException(null, claims, "Token expirado"))
                .when(gerenciadorTokenJwt).getUsernameFromToken("token.expirado.aqui");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getContentType()).startsWith("application/json");
    }

    @Test
    @DisplayName("Deve continuar o filter chain quando não há token na requisição")
    void deveContinuarFilterChainQuandoNaoHaToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("Deve continuar filter chain quando token está presente mas username é nulo")
    void deveContinuarFilterChainQuandoUsernameNulo() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.qualquer");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(gerenciadorTokenJwt.getUsernameFromToken("token.qualquer")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
