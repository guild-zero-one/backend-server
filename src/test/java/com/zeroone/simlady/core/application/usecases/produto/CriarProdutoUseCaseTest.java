package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private CriarProdutoUseCase criarProdutoUseCase;

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        // Given
        Produto produto = Produto.newProduto(
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto.jpg"
        );

        Produto produtoSalvo = Produto.of(
                UUID.randomUUID(),
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

        when(repository.salvarProduto(any(Produto.class))).thenReturn(produtoSalvo);

        // When
        Produto resultado = criarProdutoUseCase.executar(produto);

        // Then
        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku().getValue());
        assertEquals("Descrição do produto", resultado.getDescricao().getValue());
        assertEquals("tag1", resultado.getTag());
        assertEquals(10, resultado.getQuantidade().getValue());
        assertEquals(100.0, resultado.getPrecoUnitario().getValue());
        assertTrue(resultado.getCatalogo());
        assertEquals(120.0, resultado.getValorVenda().getValue());
        assertEquals("http://imagem.com/produto.jpg", resultado.getImagemUrl().getValue());

        verify(repository).salvarProduto(produto);
    }

    @Test
    @DisplayName("Deve criar produto sem imagem")
    void deveCriarProdutoSemImagem() {
        // Given
        Produto produto = Produto.newProduto(
                "Produto Teste",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                null
        );

        when(repository.salvarProduto(any(Produto.class))).thenReturn(produto);

        // When
        Produto resultado = criarProdutoUseCase.executar(produto);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getImagemUrl());
        verify(repository).salvarProduto(produto);
    }

    @Test
    @DisplayName("Deve criar produto com marca")
    void deveCriarProdutoComMarca() {
        // Given
        UUID marcaId = UUID.randomUUID();
        Produto produto = Produto.of(
                UUID.randomUUID(),
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

        when(repository.salvarProduto(any(Produto.class))).thenReturn(produto);

        // When
        Produto resultado = criarProdutoUseCase.executar(produto);

        // Then
        assertNotNull(resultado);
        assertEquals(marcaId, resultado.getIdMarca());
        verify(repository).salvarProduto(produto);
    }
}
