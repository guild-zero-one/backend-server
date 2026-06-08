package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Categoria;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.CategoriaRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private FornecedorService fornecedorService;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve listar produtos quando existirem registros")
    void deveListarProdutosQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Produto produto = new Produto();
        UUID id = UUID.randomUUID();
        produto.setId(id);
        produto.setNome("Produto Teste");
        produto.setSku("SKU-123");
        produto.setQuantidade(10);
        produto.setPrecoUnitario(100.0);
        produto.setUrlImagem("https://imagem.com/produto.png");

        List<Produto> produtos = List.of(produto);
        Page<Produto> page = new PageImpl<>(produtos);

        // When
        when(produtoRepository.findAll(pageable)).thenReturn(page);

        // Then
        Page<Produto> resultado = produtoService.listar(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Produto Teste", resultado.getContent().getFirst().getNome());
        assertEquals("https://imagem.com/produto.png", resultado.getContent().getFirst().getUrlImagem());
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
        produto.setUrlImagem("https://imagem.com/produto.png");

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // When
        Produto resultado = produtoService.cadastrarProduto(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku());
        assertEquals("https://imagem.com/produto.png", resultado.getUrlImagem());
        verify(produtoRepository).save(produto);
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto Teste");
        produto.setSku("SKU-123");
        produto.setUrlImagem("https://imagem.com/produto.png");

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // When
        Produto resultado = produtoService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("https://imagem.com/produto.png", resultado.getUrlImagem());
        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar produto por ID inexistente")
    void deveLancarExcecaoQuandoBuscarProdutoPorIdInexistente() {
        // Given
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarPorId(id));
        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Produto Antigo");
        produtoExistente.setSku("SKU-123");
        produtoExistente.setQuantidade(10);
        produtoExistente.setPrecoUnitario(100.0);
        produtoExistente.setUrlImagem("https://imagem.com/antigo.png");

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Novo");
        produtoAtualizado.setSku("SKU-456");
        produtoAtualizado.setQuantidade(20);
        produtoAtualizado.setPrecoUnitario(200.0);
        produtoAtualizado.setUrlImagem("https://imagem.com/novo.png");

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        Produto resultado = produtoService.atualizar(id, produtoAtualizado);

        // Assert
        assertEquals("Produto Novo", resultado.getNome());
        assertEquals("SKU-456", resultado.getSku());
        assertEquals(20, resultado.getQuantidade());
        assertEquals(200.0, resultado.getPrecoUnitario());
        assertEquals("https://imagem.com/novo.png", resultado.getUrlImagem());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve buscar produtos por fornecedor com sucesso")
    void deveBuscarProdutosPorFornecedorComSucesso() {
        // Given
        UUID fornecedorId = UUID.randomUUID();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");
        produto.setUrlImagem("https://imagem.com/produto.png");

        List<Produto> produtos = List.of(produto);

        when(produtoRepository.findByFornecedorId(fornecedorId)).thenReturn(produtos);

        // When
        List<Produto> resultado = produtoService.buscarPorFornecedor(fornecedorId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.getFirst().getNome());
        assertEquals("https://imagem.com/produto.png", resultado.getFirst().getUrlImagem());
        verify(produtoRepository).findByFornecedorId(fornecedorId);
    }

    @Test
    @DisplayName("Deve listar produtos por fornecedor filtrando pelo campo catalogo quando informado")
    void deveListarProdutosPorFornecedorFiltrandoPorCatalogoQuandoInformado() {
        // Given
        UUID fornecedorId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setCatalogo(true);
        Page<Produto> page = new PageImpl<>(List.of(produto));

        when(produtoRepository.findByFornecedorIdAndCatalogo(fornecedorId, true, pageable)).thenReturn(page);

        // When
        Page<Produto> resultado = produtoService.listarPorFornecedor(fornecedorId, true, pageable);

        // Then
        assertEquals(page, resultado);
        verify(produtoRepository).findByFornecedorIdAndCatalogo(fornecedorId, true, pageable);
    }

    @Test
    @DisplayName("Deve listar produtos por fornecedor sem filtrar pelo catalogo quando parâmetro for omitido")
    void deveListarProdutosPorFornecedorSemFiltrarPorCatalogoQuandoOmitido() {
        // Given
        UUID fornecedorId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();
        Page<Produto> page = new PageImpl<>(List.of(new Produto()));

        when(produtoRepository.findByFornecedorIdAndCatalogo(fornecedorId, null, pageable)).thenReturn(page);

        // When
        Page<Produto> resultado = produtoService.listarPorFornecedor(fornecedorId, null, pageable);

        // Then
        assertEquals(page, resultado);
        verify(produtoRepository).findByFornecedorIdAndCatalogo(fornecedorId, null, pageable);
    }

    @Test
    @DisplayName("Deve listar produtos por fornecedor com filtro de nome e catalogo")
    void deveListarProdutosPorFornecedorComFiltroDeNomeECatalogo() {
        // Given
        UUID fornecedorId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();
        Page<Produto> page = new PageImpl<>(List.of(new Produto()));

        when(produtoRepository.findByFornecedorIdAndNomeContainingIgnoreCaseAndCatalogo(fornecedorId, "batom", false, pageable))
                .thenReturn(page);

        // When
        Page<Produto> resultado = produtoService.listarPorFornecedorComFiltro(fornecedorId, " batom ", false, pageable);

        // Then
        assertEquals(page, resultado);
        verify(produtoRepository).findByFornecedorIdAndNomeContainingIgnoreCaseAndCatalogo(fornecedorId, "batom", false, pageable);
    }

    @Test
    @DisplayName("Deve excluir produto com sucesso")
    void deveExcluirProdutoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        produtoService.excluirPorId(id);

        // Then
        verify(produtoRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar produto inexistente")
    void deveLancarExcecaoAoTentarAtualizarProdutoInexistente() {
        // Given
        UUID id = UUID.randomUUID();
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
        UUID fornecedorId = UUID.randomUUID();
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
        UUID id = UUID.randomUUID();
        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Produto Original");
        produtoExistente.setSku("SKU-123");
        produtoExistente.setQuantidade(10);
        produtoExistente.setPrecoUnitario(100.0);
        produtoExistente.setUrlImagem("https://imagem.com/original.png");

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoExistente);

        // When
        Produto resultado = produtoService.atualizar(id, new Produto());

        // Assert
        assertEquals("Produto Original", resultado.getNome());
        assertEquals("SKU-123", resultado.getSku());
        assertEquals(10, resultado.getQuantidade());
        assertEquals(100.0, resultado.getPrecoUnitario());
        assertEquals("https://imagem.com/original.png", resultado.getUrlImagem());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve listar produtos por fornecedor com sucesso usando DTO")
    void deveListarProdutosPorFornecedorComSucessoUsandoDTO() {
        // Given
        UUID fornecedorId = UUID.randomUUID();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");
        produto.setUrlImagem("https://imagem.com/produto.png");

        ProdutoResponseDto produtoResponseDto = new ProdutoResponseDto();
        produtoResponseDto.setId(produto.getId());
        produtoResponseDto.setNome("Produto Teste");
        produtoResponseDto.setUrlImagem("https://imagem.com/produto.png");

        when(produtoRepository.findByFornecedorId(fornecedorId)).thenReturn(List.of(produto));
        when(produtoMapper.toResponseDto(produto)).thenReturn(produtoResponseDto);

        // When
        List<ProdutoResponseDto> resultado = produtoService.listarProdutosPorFornecedor(fornecedorId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.getFirst().getNome());
        assertEquals("https://imagem.com/produto.png", resultado.getFirst().getUrlImagem());
        verify(produtoRepository).findByFornecedorId(fornecedorId);
        verify(produtoMapper).toResponseDto(produto);
    }

    @Test
    @DisplayName("Deve associar categoria ao produto com sucesso")
    void deveAssociarCategoriaAoProdutoComSucesso() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        // When
        produtoService.associarCategoria(produtoId, categoriaId);

        // Assert
        assertTrue(categoria.getProdutos().contains(produto));
        verify(categoriaRepository).save(categoria);
    }

    @Test
    @DisplayName("Deve lançar exceção ao associar categoria a produto inexistente")
    void deveLancarExcecaoAoAssociarCategoriaAProdutoInexistente() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.associarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao associar categoria inexistente ao produto")
    void deveLancarExcecaoAoAssociarCategoriaInexistenteAoProduto() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.associarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao associar categoria já associada ao produto")
    void deveLancarExcecaoAoAssociarCategoriaJaAssociada() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.getProdutos().add(produto);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        // Then & Assert
        assertThrows(ResourceAlreadyExistsException.class,
                () -> produtoService.associarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve desassociar categoria do produto com sucesso")
    void deveDesassociarCategoriaDoProdutoComSucesso() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.getProdutos().add(produto);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        // When
        produtoService.desassociarCategoria(produtoId, categoriaId);

        // Assert
        assertFalse(categoria.getProdutos().contains(produto));
        verify(categoriaRepository).save(categoria);
    }

    @Test
    @DisplayName("Deve lançar exceção ao desassociar categoria de produto inexistente")
    void deveLancarExcecaoAoDesassociarCategoriaDeProdutoInexistente() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.desassociarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao desassociar categoria inexistente do produto")
    void deveLancarExcecaoAoDesassociarCategoriaInexistenteDoProduto() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.desassociarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao desassociar categoria não associada ao produto")
    void deveLancarExcecaoAoDesassociarCategoriaNaoAssociada() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> produtoService.desassociarCategoria(produtoId, categoriaId));
        verify(categoriaRepository, never()).save(any());
    }
}