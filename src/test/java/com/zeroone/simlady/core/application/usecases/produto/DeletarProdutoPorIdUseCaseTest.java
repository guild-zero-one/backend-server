package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
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
class DeletarProdutoPorIdUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase;

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void deveDeletarProdutoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Produto produto = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        // When
        assertDoesNotThrow(() -> deletarProdutoPorIdUseCase.executar(id));

        // Then
        verify(repository).buscarPorId(id);
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> deletarProdutoPorIdUseCase.executar(id));
        verify(repository).buscarPorId(id);
        verify(repository, never()).deletarPorId(any(UUID.class));
    }

    @Test
    @DisplayName("Deve deletar produto com marca")
    void deveDeletarProdutoComMarca() {
        // Given
        UUID id = UUID.randomUUID();
        UUID marcaId = UUID.randomUUID();
        Produto produto = Produto.of(
                id,
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto.jpg",
                marcaId
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        // When
        assertDoesNotThrow(() -> deletarProdutoPorIdUseCase.executar(id));

        // Then
        verify(repository).buscarPorId(id);
        verify(repository).deletarPorId(id);
    }
}
