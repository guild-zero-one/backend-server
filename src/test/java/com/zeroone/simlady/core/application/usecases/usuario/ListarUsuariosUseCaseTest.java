package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarUsuariosUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private ListarUsuariosUseCase listarUsuariosUseCase;

    @Test
    @DisplayName("Deve listar usuários com sucesso")
    void deveListarUsuariosComSucesso() {
        // Given
        UUID usuarioId1 = UUID.randomUUID();
        UUID usuarioId2 = UUID.randomUUID();
        
        Usuario usuario1 = Usuario.of(
                usuarioId1,
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

        Usuario usuario2 = Usuario.of(
                usuarioId2,
                "Maria Santos",
                "Santos",
                "maria@email.com",
                "senha123",
                "11999999999",
                true,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Usuario> usuarios = List.of(usuario1, usuario2);

        when(repository.listarTodos()).thenReturn(usuarios);

        // When
        List<Usuario> resultado = listarUsuariosUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());
        verify(repository).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
        // Given
        List<Usuario> usuariosVazios = List.of();

        when(repository.listarTodos()).thenReturn(usuariosVazios);

        // When
        List<Usuario> resultado = listarUsuariosUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).listarTodos();
    }

    @Test
    @DisplayName("Deve listar usuários ativos e inativos")
    void deveListarUsuariosAtivosEInativos() {
        // Given
        UUID usuarioId1 = UUID.randomUUID();
        UUID usuarioId2 = UUID.randomUUID();
        
        Usuario usuarioAtivo = Usuario.of(
                usuarioId1,
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

        Usuario usuarioInativo = Usuario.of(
                usuarioId2,
                "Maria Santos",
                "Santos",
                "maria@email.com",
                "senha123",
                "11999999999",
                false,
                com.zeroone.simlady.core.domain.usuario.Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Usuario> usuarios = List.of(usuarioAtivo, usuarioInativo);

        when(repository.listarTodos()).thenReturn(usuarios);

        // When
        List<Usuario> resultado = listarUsuariosUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).isAtivo());
        assertFalse(resultado.get(1).isAtivo());
        verify(repository).listarTodos();
    }
}
