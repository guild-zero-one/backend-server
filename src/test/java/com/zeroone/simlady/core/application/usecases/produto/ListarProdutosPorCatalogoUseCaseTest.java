package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarProdutosPorCatalogoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private ListarProdutosPorCatalogoUseCase listarProdutosPorCatalogoUseCase;
    
    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    @DisplayName("Deve listar produtos do catálogo com sucesso")
    void deveListarProdutosDoCatalogoComSucesso() {
        // Given
        Boolean catalogo = true;
        int pagina = 0;
        int tamanho = 10;
        
        Produto produto1 = Produto.of(
                UUID.randomUUID(),
                "Produto 1",
                "SKU-001",
                "Descrição 1",
                "tag1",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/produto1.jpg",
                null
        );

        Produto produto2 = Produto.of(
                UUID.randomUUID(),
                "Produto 2",
                "SKU-002",
                "Descrição 2",
                "tag2",
                5,
                200.0,
                true,
                250.0,
                "http://imagem.com/produto2.jpg",
                null
        );

        List<Produto> produtos = List.of(produto1, produto2);
        Page<Produto> page = new PageImpl<>(produtos, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarPorCatalogo(catalogo, pagina, tamanho)).thenReturn(page);

        // When
        Page<Produto> resultado = listarProdutosPorCatalogoUseCase.executar(catalogo, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Produto 1", resultado.getContent().get(0).getNome());
        assertEquals("Produto 2", resultado.getContent().get(1).getNome());
        assertTrue(resultado.getContent().get(0).getCatalogo());
        assertTrue(resultado.getContent().get(1).getCatalogo());
        verify(repository).listarPorCatalogo(catalogo, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar produtos fora do catálogo com sucesso")
    void deveListarProdutosForaDoCatalogoComSucesso() {
        // Given
        Boolean catalogo = false;
        int pagina = 0;
        int tamanho = 10;
        
        Produto produto = Produto.of(
                UUID.randomUUID(),
                "Produto Fora do Catálogo",
                "SKU-123",
                "Descrição do produto",
                "tag1",
                10,
                100.0,
                false,
                120.0,
                "http://imagem.com/produto.jpg",
                null
        );

        List<Produto> produtos = List.of(produto);
        Page<Produto> page = new PageImpl<>(produtos, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarPorCatalogo(catalogo, pagina, tamanho)).thenReturn(page);

        // When
        Page<Produto> resultado = listarProdutosPorCatalogoUseCase.executar(catalogo, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertEquals("Produto Fora do Catálogo", resultado.getContent().get(0).getNome());
        assertFalse(resultado.getContent().get(0).getCatalogo());
        verify(repository).listarPorCatalogo(catalogo, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há produtos no catálogo")
    void deveRetornarPaginaVaziaQuandoNaoHaProdutosNoCatalogo() {
        // Given
        Boolean catalogo = true;
        int pagina = 0;
        int tamanho = 10;
        Page<Produto> pageVazia = new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 0);

        when(repository.listarPorCatalogo(catalogo, pagina, tamanho)).thenReturn(pageVazia);

        // When
        Page<Produto> resultado = listarProdutosPorCatalogoUseCase.executar(catalogo, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(repository).listarPorCatalogo(catalogo, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar produtos do catálogo com paginação")
    void deveListarProdutosDoCatalogoComPaginacao() {
        // Given
        Boolean catalogo = true;
        int pagina = 1;
        int tamanho = 5;
        
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
                null
        );

        List<Produto> produtos = List.of(produto);
        Page<Produto> page = new PageImpl<>(produtos, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarPorCatalogo(eq(catalogo), eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Produto> resultado = listarProdutosPorCatalogoUseCase.executar(catalogo, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pagina, resultado.getNumber());
        assertEquals(tamanho, resultado.getSize());
        assertTrue(resultado.getContent().get(0).getCatalogo());
        verify(repository).listarPorCatalogo(catalogo, pagina, tamanho);
    }
}
