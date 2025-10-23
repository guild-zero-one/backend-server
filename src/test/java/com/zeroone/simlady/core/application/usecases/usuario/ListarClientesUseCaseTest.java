package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.usuario.Permissao;
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
class ListarClientesUseCaseTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @InjectMocks
    private ListarClientesUseCase listarClientesUseCase;

    @Test
    @DisplayName("Deve listar clientes com sucesso")
    void deveListarClientesComSucesso() {
        // Given
        Usuario cliente1 = Usuario.of(
                UUID.randomUUID(),
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        Usuario cliente2 = Usuario.of(
                UUID.randomUUID(),
                "Maria Santos",
                "Santos",
                "maria@email.com",
                "senha123",
                "11999999999",
                true,
                Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Usuario> clientes = List.of(cliente1, cliente2);

        when(repository.listarPorPermissao(Permissao.COMUM)).thenReturn(clientes);

        // When
        List<Usuario> resultado = listarClientesUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());
        assertEquals(Permissao.COMUM, resultado.get(0).getPermissao());
        assertEquals(Permissao.COMUM, resultado.get(1).getPermissao());
        verify(repository).listarPorPermissao(Permissao.COMUM);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há clientes")
    void deveRetornarListaVaziaQuandoNaoHaClientes() {
        // Given
        List<Usuario> clientesVazios = List.of();

        when(repository.listarPorPermissao(Permissao.COMUM)).thenReturn(clientesVazios);

        // When
        List<Usuario> resultado = listarClientesUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).listarPorPermissao(Permissao.COMUM);
    }

    @Test
    @DisplayName("Deve listar apenas clientes com permissão COMUM")
    void deveListarApenasClientesComPermissaoComum() {
        // Given
        Usuario cliente = Usuario.of(
                UUID.randomUUID(),
                "Pedro Silva",
                "Silva",
                "pedro@email.com",
                "senha123",
                "11999999999",
                true,
                Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Usuario> clientes = List.of(cliente);

        when(repository.listarPorPermissao(Permissao.COMUM)).thenReturn(clientes);

        // When
        List<Usuario> resultado = listarClientesUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Pedro Silva", resultado.get(0).getNome());
        assertEquals(Permissao.COMUM, resultado.get(0).getPermissao());
        verify(repository).listarPorPermissao(Permissao.COMUM);
    }

    @Test
    @DisplayName("Deve listar clientes ativos e inativos")
    void deveListarClientesAtivosEInativos() {
        // Given
        Usuario clienteAtivo = Usuario.of(
                UUID.randomUUID(),
                "João Silva",
                "Silva",
                "joao@email.com",
                "senha123",
                "11999999999",
                true,
                Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        Usuario clienteInativo = Usuario.of(
                UUID.randomUUID(),
                "Maria Santos",
                "Santos",
                "maria@email.com",
                "senha123",
                "11999999999",
                false,
                Permissao.COMUM,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        List<Usuario> clientes = List.of(clienteAtivo, clienteInativo);

        when(repository.listarPorPermissao(Permissao.COMUM)).thenReturn(clientes);

        // When
        List<Usuario> resultado = listarClientesUseCase.executar();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).isAtivo());
        assertFalse(resultado.get(1).isAtivo());
        verify(repository).listarPorPermissao(Permissao.COMUM);
    }
}
