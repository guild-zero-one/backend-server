package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
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
class AlterarStatusCatalogoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private AlterarStatusCatalogoUseCase alterarStatusCatalogoUseCase;

    @Test
    @DisplayName("Deve alterar status do catálogo para ativo com sucesso")
    void deveAlterarStatusDoCatalogoParaAtivoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean novoStatus = true;
        
        Produto produtoExistente = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                false, // Status atual: fora do catálogo
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        Produto produtoAtualizado = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true, // Novo status: no catálogo
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.atualizarProduto(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Optional<Produto> resultado = alterarStatusCatalogoUseCase.executar(id, novoStatus);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(novoStatus, resultado.get().getCatalogo());
        assertTrue(resultado.get().estaNoCatalogo());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarProduto(any(Produto.class));
    }

    @Test
    @DisplayName("Deve alterar status do catálogo para inativo com sucesso")
    void deveAlterarStatusDoCatalogoParaInativoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean novoStatus = false;
        
        Produto produtoExistente = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true, // Status atual: no catálogo
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        Produto produtoAtualizado = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                false, // Novo status: fora do catálogo
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.atualizarProduto(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Optional<Produto> resultado = alterarStatusCatalogoUseCase.executar(id, novoStatus);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(novoStatus, resultado.get().getCatalogo());
        assertFalse(resultado.get().estaNoCatalogo());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarProduto(any(Produto.class));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando produto não for encontrado")
    void deveRetornarOptionalVazioQuandoProdutoNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean novoStatus = true;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<Produto> resultado = alterarStatusCatalogoUseCase.executar(id, novoStatus);

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(id);
        verify(repository, never()).atualizarProduto(any(Produto.class));
    }

    @Test
    @DisplayName("Deve manter status atual quando novo status for igual ao atual")
    void deveManterStatusAtualQuandoNovoStatusForIgualAoAtual() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean statusAtual = true;
        Boolean novoStatus = true; // Mesmo status
        
        Produto produtoExistente = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                statusAtual,
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.atualizarProduto(any(Produto.class))).thenReturn(produtoExistente);

        // When
        Optional<Produto> resultado = alterarStatusCatalogoUseCase.executar(id, novoStatus);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(statusAtual, resultado.get().getCatalogo());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarProduto(any(Produto.class));
    }
}
