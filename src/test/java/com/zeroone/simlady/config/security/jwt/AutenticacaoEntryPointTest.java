package com.zeroone.simlady.config.security.jwt;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AutenticacaoEntryPointTest {

    private AutenticacaoEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        entryPoint = new AutenticacaoEntryPoint();
    }

    @Test
    @DisplayName("Deve retornar status 401 quando token estiver ausente ou inválido")
    void deveRetornar401QuandoTokenAusenteOuInvalido() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("Token inválido"));

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("Deve definir Content-Type application/json quando não autorizado")
    void deveDefinirContentTypeJsonQuandoNaoAutorizado() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("Token inválido"));

        assertThat(response.getContentType()).startsWith("application/json");
    }

    @Test
    @DisplayName("Deve escrever campo message no body JSON quando não autorizado")
    void deveEscreverBodyComCampoMessageQuandoNaoAutorizado() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("Token inválido"));

        assertThat(response.getContentAsString()).contains("message");
        assertThat(response.getContentAsString()).contains("Token inválido ou ausente.");
    }

    @Test
    @DisplayName("Deve definir charset UTF-8 quando não autorizado")
    void deveDefinirCharsetUtf8QuandoNaoAutorizado() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("Token inválido"));

        assertThat(response.getCharacterEncoding()).isEqualToIgnoringCase("UTF-8");
    }
}
