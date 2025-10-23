package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceAlreadyExistsException;
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
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.of(id, "João Silva", "Silva", "joao@email.com", "senha123", "11999999999", true, com.zeroone.simlady.core.domain.usuario.Permissao.COMUM, null, null);

        Usuario usuarioSalvo = Usuario.of(id, "João Silva", "Silva", "joao@email.com", "senha123", "11999999999", true, com.zeroone.simlady.core.domain.usuario.Permissao.COMUM, null, null);

        when(repository.buscarPorEmail("joao@email.com")).thenReturn(Optional.empty());
        when(repository.salvarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When
        Usuario resultado = cadastrarUsuarioUseCase.executar(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertTrue(resultado.isAtivo());
        verify(repository).buscarPorEmail("joao@email.com");
        verify(repository).salvarUsuario(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existir")
    void deveLancarExcecaoQuandoEmailJaExistir() {
        // Given
        Usuario usuario = Usuario.newUsuario("João Silva", "Silva", "joao@email.com", "senha123", "11999999999", com.zeroone.simlady.core.domain.usuario.Permissao.COMUM);

        Usuario usuarioExistente = Usuario.of(UUID.randomUUID(), "João Silva", "Silva", "joao@email.com", "senha123", "11999999999", true, com.zeroone.simlady.core.domain.usuario.Permissao.COMUM, null, null);

        when(repository.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));

        // When & Then
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, 
                () -> cadastrarUsuarioUseCase.executar(usuario));
        
        assertEquals("Usuário já existe com o email: joao@email.com", exception.getMessage());
        verify(repository).buscarPorEmail("joao@email.com");
        verify(repository, never()).salvarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve cadastrar usuário com dados mínimos")
    void deveCadastrarUsuarioComDadosMinimos() {
        // Given
        Usuario usuario = Usuario.newUsuario("Maria Silva", "Silva", "maria@email.com", "senha123", "11999999999", com.zeroone.simlady.core.domain.usuario.Permissao.COMUM);

        when(repository.buscarPorEmail("maria@email.com")).thenReturn(Optional.empty());
        when(repository.salvarUsuario(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = cadastrarUsuarioUseCase.executar(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
        verify(repository).buscarPorEmail("maria@email.com");
        verify(repository).salvarUsuario(usuario);
    }
}
