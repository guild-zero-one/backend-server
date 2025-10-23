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
class BuscarProdutoPorIdUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
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
        Produto resultado = buscarProdutoPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku().getValue());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> buscarProdutoPorIdUseCase.executar(id));
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar produto com marca")
    void deveBuscarProdutoComMarca() {
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
        Produto resultado = buscarProdutoPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(marcaId, resultado.getIdMarca());
        verify(repository).buscarPorId(id);
    }
}
