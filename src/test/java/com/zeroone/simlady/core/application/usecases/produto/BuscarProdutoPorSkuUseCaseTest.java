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
class BuscarProdutoPorSkuUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private BuscarProdutoPorSkuUseCase buscarProdutoPorSkuUseCase;

    @Test
    @DisplayName("Deve buscar produto por SKU com sucesso")
    void deveBuscarProdutoPorSkuComSucesso() {
        // Given
        String sku = "SKU-123";
        UUID id = UUID.randomUUID();
        Produto produto = Produto.of(
                id,
                "Produto Teste",
                sku,
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        when(repository.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        // When
        Produto resultado = buscarProdutoPorSkuUseCase.executar(sku);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals(sku, resultado.getSku().getValue());
        verify(repository).buscarPorSku(sku);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado por SKU")
    void deveLancarExcecaoQuandoProdutoNaoForEncontradoPorSku() {
        // Given
        String sku = "SKU-INEXISTENTE";
        when(repository.buscarPorSku(sku)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> buscarProdutoPorSkuUseCase.executar(sku));
        verify(repository).buscarPorSku(sku);
    }

    @Test
    @DisplayName("Deve buscar produto por SKU com marca")
    void deveBuscarProdutoPorSkuComMarca() {
        // Given
        String sku = "SKU-123";
        UUID id = UUID.randomUUID();
        UUID marcaId = UUID.randomUUID();
        Produto produto = Produto.of(
                id,
                "Produto Teste",
                sku,
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto.jpg",
                marcaId
        );

        when(repository.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        // When
        Produto resultado = buscarProdutoPorSkuUseCase.executar(sku);

        // Then
        assertNotNull(resultado);
        assertEquals(marcaId, resultado.getIdMarca());
        verify(repository).buscarPorSku(sku);
    }

    @Test
    @DisplayName("Deve buscar produto por SKU sem imagem")
    void deveBuscarProdutoPorSkuSemImagem() {
        // Given
        String sku = "SKU-123";
        UUID id = UUID.randomUUID();
        Produto produto = Produto.of(
                id,
                "Produto Teste",
                sku,
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                null,
                null
        );

        when(repository.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        // When
        Produto resultado = buscarProdutoPorSkuUseCase.executar(sku);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getImagemUrl());
        verify(repository).buscarPorSku(sku);
    }
}
