package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenAdapter;
import com.zeroone.simlady.infrastructure.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @Mock
    private JwtTokenAdapter geradorToken;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @Test
    @DisplayName("Deve autenticar usuário com sucesso")
    void deveAutenticarUsuarioComSucesso() {
        // Given
        Usuario usuario = Usuario.of(
                UUID.randomUUID(),
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioEncontrado = Usuario.of(
                usuarioId,
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        String tokenEsperado = "jwt-token-123";

        when(repository.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioEncontrado));
        when(geradorToken.gerarToken(usuarioEncontrado, response)).thenReturn(tokenEsperado);

        // When
        String token = autenticarUsuarioUseCase.executar(usuario, response);

        // Then
        assertNotNull(token);
        assertEquals(tokenEsperado, token);
        verify(repository).buscarPorEmail("joao@email.com");
        verify(geradorToken).gerarToken(usuarioEncontrado, response);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Given
        Usuario usuario = Usuario.of(
                UUID.randomUUID(),
                "Usuário",
                "Teste",
                "inexistente@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // When & Then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, 
                () -> autenticarUsuarioUseCase.executar(usuario, response));
        
        assertEquals("Email ou senha inválidos", exception.getMessage());
        verify(repository).buscarPorEmail("inexistente@email.com");
        verify(geradorToken, never()).gerarToken(any(Usuario.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário estiver inativo")
    void deveLancarExcecaoQuandoUsuarioEstiverInativo() {
        // Given
        Usuario usuario = Usuario.of(
                UUID.randomUUID(),
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioInativo = Usuario.of(
                usuarioId,
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                false,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioInativo));

        // When & Then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, 
                () -> autenticarUsuarioUseCase.executar(usuario, response));
        
        assertEquals("Usuário inativo", exception.getMessage());
        verify(repository).buscarPorEmail("joao@email.com");
        verify(geradorToken, never()).gerarToken(any(Usuario.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("Deve autenticar usuário ativo")
    void deveAutenticarUsuarioAtivo() {
        // Given
        Usuario usuario = Usuario.of(
                UUID.randomUUID(),
                "Maria Silva",
                "Silva",
                "maria@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioAtivo = Usuario.of(
                usuarioId,
                "Maria Silva",
                "Silva",
                "maria@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        String tokenEsperado = "jwt-token-maria";

        when(repository.buscarPorEmail("maria@email.com")).thenReturn(Optional.of(usuarioAtivo));
        when(geradorToken.gerarToken(usuarioAtivo, response)).thenReturn(tokenEsperado);

        // When
        String token = autenticarUsuarioUseCase.executar(usuario, response);

        // Then
        assertNotNull(token);
        assertEquals(tokenEsperado, token);
        verify(repository).buscarPorEmail("maria@email.com");
        verify(geradorToken).gerarToken(usuarioAtivo, response);
    }
}
