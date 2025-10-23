package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenExtractorAdapter;
import com.zeroone.simlady.infrastructure.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
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
class BuscarUsuarioAutenticadoUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @Mock
    private JwtTokenExtractorAdapter extratorToken;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private BuscarUsuarioAutenticadoUseCase buscarUsuarioAutenticadoUseCase;

    @Test
    @DisplayName("Deve buscar usuário autenticado com sucesso")
    void deveBuscarUsuarioAutenticadoComSucesso() {
        // Given
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = Usuario.of(
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

        when(extratorToken.extrairIdUsuario(request)).thenReturn(usuarioId);
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = buscarUsuarioAutenticadoUseCase.executar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertTrue(resultado.isAtivo());
        verify(extratorToken).extrairIdUsuario(request);
        verify(repository).buscarPorId(usuarioId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Given
        UUID usuarioId = UUID.randomUUID();

        when(extratorToken.extrairIdUsuario(request)).thenReturn(usuarioId);
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.empty());

        // When & Then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, 
                () -> buscarUsuarioAutenticadoUseCase.executar(request));
        
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(extratorToken).extrairIdUsuario(request);
        verify(repository).buscarPorId(usuarioId);
    }

    @Test
    @DisplayName("Deve buscar usuário autenticado inativo")
    void deveBuscarUsuarioAutenticadoInativo() {
        // Given
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                usuarioId,
                "Maria Silva",
                "Silva",
                "maria@email.com",
                "senha123",
                "11999999999",
                false,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(extratorToken.extrairIdUsuario(request)).thenReturn(usuarioId);
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = buscarUsuarioAutenticadoUseCase.executar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
        assertFalse(resultado.isAtivo());
        verify(extratorToken).extrairIdUsuario(request);
        verify(repository).buscarPorId(usuarioId);
    }
}
