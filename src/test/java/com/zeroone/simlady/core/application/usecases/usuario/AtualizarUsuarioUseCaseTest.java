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
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        
        Usuario usuarioExistente = Usuario.of(
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

        Usuario usuarioAtualizado = Usuario.of(
                UUID.randomUUID(),
                "João Santos",
                "Silva",
                "joao.santos@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        Usuario usuarioSalvo = Usuario.of(
                id,
                "João Santos",
                "Silva",
                "joao.santos@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuarioExistente));
        when(repository.atualizarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When
        Usuario resultado = atualizarUsuarioUseCase.executar(id, usuarioAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("João Santos", resultado.getNome());
        assertEquals("Silva", resultado.getSobrenome());
        assertEquals("joao.santos@email.com", resultado.getEmail());
        assertEquals("11999999999", resultado.getCelular());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        
        Usuario usuarioAtualizado = Usuario.of(
                UUID.randomUUID(),
                "João Santos",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> atualizarUsuarioUseCase.executar(id, usuarioAtualizado));
        
        assertEquals("Usuário não encontrado com ID: " + id, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(repository, never()).atualizarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas nome do usuário")
    void deveAtualizarApenasNomeDoUsuario() {
        // Given
        UUID id = UUID.randomUUID();
        
        Usuario usuarioExistente = Usuario.of(
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

        Usuario usuarioAtualizado = Usuario.of(
                UUID.randomUUID(),
                "João Santos",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        Usuario usuarioSalvo = Usuario.of(
                id,
                "João Santos",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuarioExistente));
        when(repository.atualizarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When
        Usuario resultado = atualizarUsuarioUseCase.executar(id, usuarioAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("João Santos", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do usuário")
    void deveAtualizarTodosOsCamposDoUsuario() {
        // Given
        UUID id = UUID.randomUUID();
        
        Usuario usuarioExistente = Usuario.of(
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

        Usuario usuarioAtualizado = Usuario.of(
                UUID.randomUUID(),
                "João Santos",
                "Silva",
                "joao.santos@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        Usuario usuarioSalvo = Usuario.of(
                id,
                "João Santos",
                "Silva",
                "joao.santos@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(usuarioExistente));
        when(repository.atualizarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When
        Usuario resultado = atualizarUsuarioUseCase.executar(id, usuarioAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("João Santos", resultado.getNome());
        assertEquals("Silva", resultado.getSobrenome());
        assertEquals("joao.santos@email.com", resultado.getEmail());
        assertEquals("11999999999", resultado.getCelular());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarUsuario(any(Usuario.class));
    }
}
