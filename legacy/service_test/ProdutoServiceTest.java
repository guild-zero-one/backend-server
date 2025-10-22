package com.zeroone.simlady.service_test;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.ProdutoRepository;
import com.zeroone.simlady.mapper.ProdutoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve listar produtos quando existirem registros")
    void deveListarProdutosQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto Teste");
        produto.setSku("SKU-123");
        produto.setQuantidade(10);
        produto.setPrecoUnitario(100.0);

        List<Produto> produtos = List.of(produto);
        Page<Produto> page = new PageImpl<>(produtos);

        // When
        when(produtoRepository.findAll(pageable)).thenReturn(page);

        // Then
        Page<Produto> resultado = produtoService.listar(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Produto Teste", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso")
    void deveCadastrarProdutoComSucesso() {
        // Given
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setSku("SKU-123");
        produto.setQuantidade(10);
        produto.setPrecoUnitario(100.0);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // When
        Produto resultado = produtoService.cadastrarProduto(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku());
        verify(produtoRepository).save(produto);
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        // Given
        Integer id = 1;
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto Teste");
        produto.setSku("SKU-123");

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // When
        Produto resultado = produtoService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Produto Teste", resultado.getNome());
        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar produto por ID inexistente")
    void deveLancarExcecaoQuandoBuscarProdutoPorIdInexistente() {
        // Given
        Integer id = 999;
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarPorId(id));
        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        // Given
        Integer id = 1;
        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Produto Antigo");
        produtoExistente.setSku("SKU-123");
        produtoExistente.setQuantidade(10);
        produtoExistente.setPrecoUnitario(100.0);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Novo");
        produtoAtualizado.setSku("SKU-456");
        produtoAtualizado.setQuantidade(20);
        produtoAtualizado.setPrecoUnitario(200.0);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Produto resultado = produtoService.atualizar(id, produtoAtualizado);

        // Assert
        assertEquals("Produto Novo", resultado.getNome());
        assertEquals("SKU-456", resultado.getSku());
        assertEquals(20, resultado.getQuantidade());
        assertEquals(200.0, resultado.getPrecoUnitario());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve buscar produtos por fornecedor com sucesso")
    void deveBuscarProdutosPorFornecedorComSucesso() {
        // Given
        Integer fornecedorId = 1;
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto Teste");

        List<Produto> produtos = List.of(produto);

        when(produtoRepository.findByFornecedorId(fornecedorId)).thenReturn(produtos);

        // When
        List<Produto> resultado = produtoService.buscarPorFornecedor(fornecedorId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.get(0).getNome());
        verify(produtoRepository).findByFornecedorId(fornecedorId);
    }

    @Test
    @DisplayName("Deve excluir produto com sucesso")
    void deveExcluirProdutoComSucesso() {
        // Given
        Integer id = 1;

        // When
        produtoService.excluirPorId(id);

        // Then
        verify(produtoRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar produto inexistente")
    void deveLancarExcecaoAoTentarAtualizarProdutoInexistente() {
        // Given
        Integer id = 999;
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Novo");

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizar(id, produtoAtualizado));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar produtos por fornecedor inexistente")
    void deveRetornarListaVaziaAoBuscarProdutosPorFornecedorInexistente() {
        // Given
        Integer fornecedorId = 999;
        when(produtoRepository.findByFornecedorId(fornecedorId)).thenReturn(List.of());

        // When
        List<Produto> resultado = produtoService.buscarPorFornecedor(fornecedorId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository).findByFornecedorId(fornecedorId);
    }

    @Test
    @DisplayName("Deve manter dados originais quando atualização for nula")
    void deveManterDadosOriginaisQuandoAtualizacaoForNula() {
        // Given
        Integer id = 1;
        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Produto Original");
        produtoExistente.setSku("SKU-123");
        produtoExistente.setQuantidade(10);
        produtoExistente.setPrecoUnitario(100.0);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoExistente);

        // When
        Produto resultado = produtoService.atualizar(id, new Produto());

        // Assert
        assertEquals("Produto Original", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku());
        assertEquals(10, resultado.getQuantidade());
        assertEquals(100.0, resultado.getPrecoUnitario());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve listar produtos por fornecedor com sucesso usando DTO")
    void deveListarProdutosPorFornecedorComSucessoUsandoDTO() {
        // Given
        Integer fornecedorId = 1;
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto Teste");

        ProdutoResponseDto produtoResponseDto = new ProdutoResponseDto();
        produtoResponseDto.setId(1);
        produtoResponseDto.setNome("Produto Teste");

        when(produtoRepository.findByFornecedorId(fornecedorId)).thenReturn(List.of(produto));
        when(produtoMapper.toResponseDto(produto)).thenReturn(produtoResponseDto);

        // When
        List<ProdutoResponseDto> resultado = produtoService.listarProdutosPorFornecedor(fornecedorId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.get(0).getNome());
        verify(produtoRepository).findByFornecedorId(fornecedorId);
        verify(produtoMapper).toResponseDto(produto);
    }
}