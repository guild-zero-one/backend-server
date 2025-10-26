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
class AtualizarProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort repository;

    @InjectMocks
    private AtualizarProdutoUseCase atualizarProdutoUseCase;

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        String novoNome = "Produto Atualizado";
        String novoSku = "SKU-456";
        String novaDescricao = "Nova descrição";
        String novaTag = "novaTag";
        Integer novaQuantidade = 20;
        Double novoPrecoUnitario = 150.0;
        Boolean novoCatalogo = false;
        Double novoValorVenda = 180.0;
        String novaImagemUrl = "http://imagem.com/novo.jpg";

        Produto produtoExistente = Produto.of(
                id,
                "Produto Original",
                "SKU-123",
                "Descrição original",
                "tagOriginal",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/original.jpg",
                null
        );

        Produto produtoAtualizado = Produto.of(
                id,
                novoNome,
                novoSku,
                novaDescricao,
                novaTag,
                novaQuantidade,
                novoPrecoUnitario,
                novoCatalogo,
                novoValorVenda,
                novaImagemUrl,
                null
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.atualizarProduto(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Optional<Produto> resultado = atualizarProdutoUseCase.executar(
                id, novoNome, novoSku, novaDescricao, novaTag, 
                novaQuantidade, novoPrecoUnitario, novoCatalogo, 
                novoValorVenda, novaImagemUrl, null
        );

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(novoNome, resultado.get().getNome());
        assertEquals(novoSku, resultado.get().getSku().getValue());
        assertEquals(novaDescricao, resultado.get().getDescricao().getValue());
        assertEquals(novaTag, resultado.get().getTag());
        assertEquals(novaQuantidade, resultado.get().getQuantidade().getValue());
        assertEquals(novoPrecoUnitario, resultado.get().getPrecoUnitario().getValue());
        assertEquals(novoCatalogo, resultado.get().getCatalogo());
        assertEquals(novoValorVenda, resultado.get().getValorVenda().getValue());
        assertEquals(novaImagemUrl, resultado.get().getImagemUrl().getValue());

        verify(repository).buscarPorId(id);
        verify(repository).atualizarProduto(any(Produto.class));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando produto não for encontrado")
    void deveRetornarOptionalVazioQuandoProdutoNaoForEncontrado() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<Produto> resultado = atualizarProdutoUseCase.executar(
                id, "Novo Nome", "SKU-456", "Nova descrição", "novaTag",
                20, 150.0, false, 180.0, "http://imagem.com/novo.jpg", null
        );

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(id);
        verify(repository, never()).atualizarProduto(any(Produto.class));
    }

    @Test
    @DisplayName("Deve atualizar produto mantendo dados originais quando valores são nulos")
    void deveAtualizarProdutoMantendoDadosOriginaisQuandoValoresSaoNulos() {
        // Given
        UUID id = UUID.randomUUID();
        String novoNome = "Produto Atualizado";
        
        Produto produtoExistente = Produto.of(
                id,
                "Produto Original",
                "SKU-123",
                "Descrição original",
                "tagOriginal",
                10,
                100.0,
                true,
                120.0,
                "http://imagem.com/original.jpg",
                null
        );

        Produto produtoAtualizado = Produto.of(
                id,
                novoNome,
                produtoExistente.getSku().getValue(),
                produtoExistente.getDescricao().getValue(),
                produtoExistente.getTag(),
                produtoExistente.getQuantidade().getValue(),
                produtoExistente.getPrecoUnitario().getValue(),
                produtoExistente.getCatalogo(),
                produtoExistente.getValorVenda().getValue(),
                produtoExistente.getImagemUrl() != null ? produtoExistente.getImagemUrl().getValue() : null,
                produtoExistente.getIdMarca()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.atualizarProduto(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Optional<Produto> resultado = atualizarProdutoUseCase.executar(
                id, novoNome, null, null, null, 
                null, null, null, null, null, null
        );

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(novoNome, resultado.get().getNome());
        // Os outros campos devem manter os valores originais
        assertEquals("SKU-123", resultado.get().getSku().getValue());
        assertNotNull(resultado.get().getDescricao());
        assertEquals("Descrição original", resultado.get().getDescricao().getValue());
        assertEquals("tagOriginal", resultado.get().getTag());
        assertEquals(10, resultado.get().getQuantidade().getValue());
        assertEquals(100.0, resultado.get().getPrecoUnitario().getValue());
        assertEquals(true, resultado.get().getCatalogo());
        assertEquals(120.0, resultado.get().getValorVenda().getValue());
        assertEquals("http://imagem.com/original.jpg", resultado.get().getImagemUrl().getValue());

        verify(repository).buscarPorId(id);
        verify(repository).atualizarProduto(any(Produto.class));
    }
}
