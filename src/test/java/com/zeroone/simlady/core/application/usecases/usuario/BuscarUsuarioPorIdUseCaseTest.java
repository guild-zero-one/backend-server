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
class BuscarUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
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
        Usuario resultado = buscarUsuarioPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertTrue(resultado.isAtivo());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> buscarUsuarioPorIdUseCase.executar(id));
        
        assertEquals("Usuário não encontrado com ID: " + id, exception.getMessage());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar usuário inativo")
    void deveBuscarUsuarioInativo() {
        // Given
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.of(
                id,
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

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = buscarUsuarioPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
        assertFalse(resultado.isAtivo());
        verify(repository).buscarPorId(id);
    }
}
