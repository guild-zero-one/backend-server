package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutoLoteRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoLoteServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private FornecedorService fornecedorService;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoLoteRequestDto produtoLoteDto;
    private Fornecedor fornecedor;
    private Produto produto;

    @BeforeEach
    void setUp() {
        fornecedor = new Fornecedor();
        fornecedor.setId(UUID.randomUUID());
        fornecedor.setNome("o boticário");

        produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Malbec");

        produtoLoteDto = new ProdutoLoteRequestDto();
        produtoLoteDto.setNome("Malbec");
        produtoLoteDto.setSku("MAL-100");
        produtoLoteDto.setDescricao("Perfume Malbec");
        produtoLoteDto.setQuantidade(10);
        produtoLoteDto.setUrlImagem("https://example.com/image.jpg");
        produtoLoteDto.setPrecoUnitario(30.0);
        produtoLoteDto.setCatalogo(true);
        produtoLoteDto.setValorVenda(50.0);
        produtoLoteDto.setFornecedorNome("O Boticário");
    }

    @Test
    void testCadastrarEmLote_CriarMarcaSeNaoExistir() {
        // Arrange
        List<ProdutoLoteRequestDto> lista = new ArrayList<>();
        lista.add(produtoLoteDto);

        when(fornecedorService.buscarPorNomeExato(anyString())).thenReturn(null);
        when(fornecedorService.cadastrarFornecedor(any(Fornecedor.class))).thenReturn(fornecedor);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoMapper.toResponseDto(any(Produto.class))).thenReturn(null);

        // Act
        ProdutoLoteResponseDto response = produtoService.cadastrarEmLote(lista);

        // Assert
        assertThat(response.getTotalProcessado()).isEqualTo(1);
        assertThat(response.getTotalCriado()).isEqualTo(1);
        assertThat(response.getTotalErro()).isEqualTo(0);

        verify(fornecedorService, times(1)).buscarPorNomeExato(anyString());
        verify(fornecedorService, times(1)).cadastrarFornecedor(any(Fornecedor.class));
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testCadastrarEmLote_AssociarMarcaExistente() {
        // Arrange
        List<ProdutoLoteRequestDto> lista = new ArrayList<>();
        lista.add(produtoLoteDto);

        when(fornecedorService.buscarPorNomeExato(anyString())).thenReturn(fornecedor);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoMapper.toResponseDto(any(Produto.class))).thenReturn(null);

        // Act
        ProdutoLoteResponseDto response = produtoService.cadastrarEmLote(lista);

        // Assert
        assertThat(response.getTotalProcessado()).isEqualTo(1);
        assertThat(response.getTotalCriado()).isEqualTo(1);
        assertThat(response.getTotalErro()).isEqualTo(0);

        verify(fornecedorService, times(1)).buscarPorNomeExato(anyString());
        verify(fornecedorService, never()).cadastrarFornecedor(any(Fornecedor.class));
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testCadastrarEmLote_ComErro() {
        // Arrange
        List<ProdutoLoteRequestDto> lista = new ArrayList<>();
        lista.add(produtoLoteDto);

        when(fornecedorService.buscarPorNomeExato(anyString())).thenThrow(new RuntimeException("Erro no banco"));

        // Act
        ProdutoLoteResponseDto response = produtoService.cadastrarEmLote(lista);

        // Assert
        assertThat(response.getTotalProcessado()).isEqualTo(1);
        assertThat(response.getTotalCriado()).isEqualTo(0);
        assertThat(response.getTotalErro()).isEqualTo(1);
        assertThat(response.getProdutosErro()).hasSize(1);
    }
}

