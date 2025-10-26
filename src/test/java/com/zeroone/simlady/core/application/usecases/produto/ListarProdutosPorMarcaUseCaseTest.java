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
class ListarProdutosPorMarcaUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private ListarProdutosPorMarcaUseCase listarProdutosPorMarcaUseCase;
    
    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    @DisplayName("Deve listar produtos por marca com sucesso")
    void deveListarProdutosPorMarcaComSucesso() {
        // Given
        UUID marcaId = UUID.randomUUID();
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
                marcaId
        );

        Produto produto2 = Produto.of(
                UUID.randomUUID(),
                "Produto 2",
                "SKU-002",
                "Descrição 2",
                "tag2",
                5,
                200.0,
                false,
                250.0,
                "http://imagem.com/produto2.jpg",
                marcaId
        );

        List<Produto> produtos = List.of(produto1, produto2);
        Page<Produto> page = new PageImpl<>(produtos, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarPorMarca(eq(marcaId), eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Produto> resultado = listarProdutosPorMarcaUseCase.executar(marcaId, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Produto 1", resultado.getContent().get(0).getNome());
        assertEquals("Produto 2", resultado.getContent().get(1).getNome());
        assertEquals(marcaId, resultado.getContent().get(0).getIdMarca());
        assertEquals(marcaId, resultado.getContent().get(1).getIdMarca());
        verify(repository).listarPorMarca(marcaId, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há produtos da marca")
    void deveRetornarPaginaVaziaQuandoNaoHaProdutosDaMarca() {
        // Given
        UUID marcaId = UUID.randomUUID();
        int pagina = 0;
        int tamanho = 10;
        Page<Produto> pageVazia = new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 0);

        when(repository.listarPorMarca(marcaId, pagina, tamanho)).thenReturn(pageVazia);

        // When
        Page<Produto> resultado = listarProdutosPorMarcaUseCase.executar(marcaId, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(repository).listarPorMarca(marcaId, pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar produtos por marca com paginação")
    void deveListarProdutosPorMarcaComPaginacao() {
        // Given
        UUID marcaId = UUID.randomUUID();
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
                marcaId
        );

        List<Produto> produtos = List.of(produto);
        Page<Produto> page = new PageImpl<>(produtos, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarPorMarca(eq(marcaId), eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Produto> resultado = listarProdutosPorMarcaUseCase.executar(marcaId, pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pagina, resultado.getNumber());
        assertEquals(tamanho, resultado.getSize());
        assertEquals(marcaId, resultado.getContent().get(0).getIdMarca());
        verify(repository).listarPorMarca(marcaId, pagina, tamanho);
    }
}
