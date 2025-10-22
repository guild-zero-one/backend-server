package com.zeroone.simlady.service_test;

import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.entity.ProdutoImagem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.ProdutoImagemRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoImagemServiceTest {

    @Mock
    private ProdutoImagemRepository produtoImagemRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoImagemService produtoImagemService;

    @Test
    @DisplayName("Deve cadastrar imagem com sucesso")
    void deveCadastrarImagemComSucesso() {
        // Given
        Produto produto = new Produto();
        produto.setId(1);

        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setUrlImagem("http://exemplo.com/imagem.jpg");
        imagem.setImagemPrincipal(true);
        imagem.setProduto(produto);

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoImagemRepository.save(any(ProdutoImagem.class))).thenReturn(imagem);

        // When
        ProdutoImagem resultado = produtoImagemService.cadastrarImagem(imagem);

        // Assert
        assertNotNull(resultado);
        assertEquals("http://exemplo.com/imagem.jpg", resultado.getUrlImagem());
        assertTrue(resultado.getImagemPrincipal());
        verify(produtoImagemRepository).save(any(ProdutoImagem.class));
    }

    @Test
    @DisplayName("Deve listar imagens quando existirem registros")
    void deveListarImagensQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(1);
        imagem.setUrlImagem("http://exemplo.com/imagem.jpg");

        List<ProdutoImagem> imagens = List.of(imagem);
        Page<ProdutoImagem> page = new PageImpl<>(imagens);

        when(produtoImagemRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<ProdutoImagem> resultado = produtoImagemService.listarImagens(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("http://exemplo.com/imagem.jpg", resultado.getContent().getFirst().getUrlImagem());
    }

    @Test
    @DisplayName("Deve buscar imagem por ID com sucesso")
    void deveBuscarImagemPorIdComSucesso() {
        // Given
        Integer id = 1;
        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(id);
        imagem.setUrlImagem("http://exemplo.com/imagem.jpg");

        when(produtoImagemRepository.findById(id)).thenReturn(Optional.of(imagem));

        // When
        ProdutoImagem resultado = produtoImagemService.buscarImagemPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("http://exemplo.com/imagem.jpg", resultado.getUrlImagem());
    }

    @Test
    @DisplayName("Deve buscar imagens por produto com sucesso")
    void deveBuscarImagensPorProdutoComSucesso() {
        // Given
        Integer produtoId = 1;
        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(1);
        imagem.setUrlImagem("http://exemplo.com/imagem.jpg");

        when(produtoImagemRepository.findByProdutoId(produtoId)).thenReturn(List.of(imagem));

        // When
        List<ProdutoImagem> resultado = produtoImagemService.buscarPorProduto(produtoId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("http://exemplo.com/imagem.jpg", resultado.getFirst().getUrlImagem());
    }

    @Test
    @DisplayName("Deve atualizar imagem com sucesso")
    void deveAtualizarImagemComSucesso() {
        // Given
        Integer id = 1;
        LocalDate dataCriacao = LocalDate.now().minusDays(1);
        LocalDate dataAtualizacao = LocalDate.now();

        Produto produto = new Produto();
        produto.setId(1);

        ProdutoImagem imagemExistente = new ProdutoImagem();
        imagemExistente.setId(id);
        imagemExistente.setUrlImagem("http://exemplo.com/imagem-antiga.jpg");
        imagemExistente.setImagemPrincipal(false);
        imagemExistente.setProduto(produto);
        imagemExistente.setDataCriacao(dataCriacao);
        imagemExistente.setDataAtualizacao(dataAtualizacao);

        ProdutoImagem imagemAtualizada = new ProdutoImagem();
        imagemAtualizada.setUrlImagem("http://exemplo.com/imagem-nova.jpg");
        imagemAtualizada.setImagemPrincipal(true);
        imagemAtualizada.setProduto(produto);

        when(produtoImagemRepository.findById(id)).thenReturn(Optional.of(imagemExistente));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoImagemRepository.save(any(ProdutoImagem.class))).thenReturn(imagemAtualizada);

        // When
        ProdutoImagem resultado = produtoImagemService.atualizarImagem(id, imagemAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals("http://exemplo.com/imagem-nova.jpg", resultado.getUrlImagem());
        assertTrue(resultado.getImagemPrincipal());
        verify(produtoImagemRepository).save(any(ProdutoImagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar imagem inexistente")
    void deveLancarExcecaoAoTentarAtualizarImagemInexistente() {
        // Given
        Integer id = 999;
        ProdutoImagem imagemAtualizada = new ProdutoImagem();
        imagemAtualizada.setUrlImagem("http://exemplo.com/imagem.jpg");

        when(produtoImagemRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoImagemService.atualizarImagem(id, imagemAtualizada));
        verify(produtoImagemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir imagem com sucesso")
    void deveExcluirImagemComSucesso() {
        // Given
        Integer id = 1;
        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(id);

        when(produtoImagemRepository.findById(id)).thenReturn(Optional.of(imagem));

        // When
        produtoImagemService.deletarImagem(id);

        // Then
        verify(produtoImagemRepository).delete(imagem);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir imagem inexistente")
    void deveLancarExcecaoAoTentarExcluirImagemInexistente() {
        // Given
        Integer id = 999;
        when(produtoImagemRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoImagemService.deletarImagem(id));
        verify(produtoImagemRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar imagem com produto inexistente")
    void deveLancarExcecaoAoCadastrarImagemComProdutoInexistente() {
        // Given
        Produto produto = new Produto();
        produto.setId(999);

        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setUrlImagem("http://exemplo.com/imagem.jpg");
        imagem.setImagemPrincipal(true);
        imagem.setProduto(produto);

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoImagemService.cadastrarImagem(imagem));
        verify(produtoImagemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar imagens por produto inexistente")
    void deveRetornarListaVaziaAoBuscarImagensPorProdutoInexistente() {
        // Given
        Integer produtoId = 999;
        when(produtoImagemRepository.findByProdutoId(produtoId)).thenReturn(List.of());

        // When
        List<ProdutoImagem> resultado = produtoImagemService.buscarPorProduto(produtoId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoImagemRepository).findByProdutoId(produtoId);
    }

    @Test
    @DisplayName("Deve manter datas originais ao atualizar imagem")
    void deveManterDatasOriginaisAoAtualizarImagem() {
        // Given
        Integer id = 1;
        LocalDate dataCriacao = LocalDate.now().minusDays(2);
        LocalDate dataAtualizacao = LocalDate.now().minusDays(1);

        Produto produto = new Produto();
        produto.setId(1);

        ProdutoImagem imagemExistente = new ProdutoImagem();
        imagemExistente.setId(id);
        imagemExistente.setUrlImagem("http://exemplo.com/imagem-antiga.jpg");
        imagemExistente.setProduto(produto);
        imagemExistente.setDataCriacao(dataCriacao);
        imagemExistente.setDataAtualizacao(dataAtualizacao);

        ProdutoImagem imagemAtualizada = new ProdutoImagem();
        imagemAtualizada.setUrlImagem("http://exemplo.com/imagem-nova.jpg");
        imagemAtualizada.setProduto(produto);

        when(produtoImagemRepository.findById(id)).thenReturn(Optional.of(imagemExistente));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoImagemRepository.save(any(ProdutoImagem.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        ProdutoImagem resultado = produtoImagemService.atualizarImagem(id, imagemAtualizada);

        // Assert
        assertEquals(dataCriacao, resultado.getDataCriacao());
        assertNotEquals(dataAtualizacao, resultado.getDataAtualizacao());
        verify(produtoImagemRepository).save(any(ProdutoImagem.class));
    }

    @Test
    @DisplayName("Deve retornar null ao buscar imagem por ID inexistente")
    void deveRetornarNullAoBuscarImagemPorIdInexistente() {
        // Given
        Integer id = 999;
        when(produtoImagemRepository.findById(id)).thenReturn(Optional.empty());

        // When
        ProdutoImagem resultado = produtoImagemService.buscarImagemPorId(id);

        // Assert
        assertNull(resultado);
        verify(produtoImagemRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não existirem imagens")
    void deveRetornarPaginaVaziaQuandoNaoExistiremImagens() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<ProdutoImagem> paginaVazia = new PageImpl<>(List.of());
        when(produtoImagemRepository.findAll(pageable)).thenReturn(paginaVazia);

        // When
        Page<ProdutoImagem> resultado = produtoImagemService.listarImagens(pageable);

        // Assert
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(produtoImagemRepository).findAll(pageable);
    }
}