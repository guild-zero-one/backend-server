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
class DeletarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private DeletarUsuarioUseCase deletarUsuarioUseCase;

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                id,
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

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuario));

        // When
        assertDoesNotThrow(() -> deletarUsuarioUseCase.executar(id));

        // Then
        verify(repository).buscarPorId(id);
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> deletarUsuarioUseCase.executar(id));
        
        assertEquals("Usuário não encontrado com ID: " + id, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(repository, never()).deletarPorId(any(UUID.class));
    }

    @Test
    @DisplayName("Deve deletar usuário ativo")
    void deveDeletarUsuarioAtivo() {
        // Given
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                id,
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

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuario));

        // When
        assertDoesNotThrow(() -> deletarUsuarioUseCase.executar(id));

        // Then
        verify(repository).buscarPorId(id);
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve deletar usuário inativo")
    void deveDeletarUsuarioInativo() {
        // Given
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                id,
                "Pedro Silva",
                "Silva",
                "pedro@email.com",
                "senha123",
                "11999999999",
                false,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuario));

        // When
        assertDoesNotThrow(() -> deletarUsuarioUseCase.executar(id));

        // Then
        verify(repository).buscarPorId(id);
        verify(repository).deletarPorId(id);
    }
}
