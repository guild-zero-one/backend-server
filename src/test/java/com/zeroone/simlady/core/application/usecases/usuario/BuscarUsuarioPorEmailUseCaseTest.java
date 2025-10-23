package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;
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
class BuscarUsuarioPorEmailUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void deveBuscarUsuarioPorEmailComSucesso() {
        // Given
        String email = "joao@email.com";
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                usuarioId,
                "João Silva",
                "Silva",
                email,
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = buscarUsuarioPorEmailUseCase.executar(email);

        // Then
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("João Silva", resultado.getNome());
        assertTrue(resultado.isAtivo());
        verify(repository).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado por email")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontradoPorEmail() {
        // Given
        String email = "inexistente@email.com";
        when(repository.buscarPorEmail(email)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> buscarUsuarioPorEmailUseCase.executar(email));
        
        assertEquals("Usuário não encontrado com email: " + email, exception.getMessage());
        verify(repository).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve buscar usuário inativo por email")
    void deveBuscarUsuarioInativoPorEmail() {
        // Given
        String email = "maria@email.com";
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                usuarioId,
                "Maria Silva",
                "Silva",
                email,
                "senha123",
                "11999999999",
                false,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = buscarUsuarioPorEmailUseCase.executar(email);

        // Then
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("Maria Silva", resultado.getNome());
        assertFalse(resultado.isAtivo());
        verify(repository).buscarPorEmail(email);
    }
}
