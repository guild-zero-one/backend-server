package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.repository.FornecedorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    @DisplayName("Deve listar fornecedores quando existirem registros")
    void deveListarFornecedoresQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Fornecedor fornecedor1 = new Fornecedor();
        UUID id = UUID.randomUUID();
        fornecedor1.setId(id);
        fornecedor1.setNome("Boticário");
        fornecedor1.setCnpj("12345678901234");
        fornecedor1.setDescricao("Descrição teste");
        fornecedor1.setImagemUrl("url/imagem.jpg");
        fornecedor1.setCriadoEm(LocalDateTime.now());
        fornecedor1.setAtualizadoEm(LocalDateTime.now());

        List<Fornecedor> fornecedores = List.of(fornecedor1);
        Page<Fornecedor> page = new PageImpl<>(fornecedores);

        // When
        when(fornecedorRepository.findAll(pageable)).thenReturn(page);

        // Then
        Page<Fornecedor> resultado = fornecedorService.listar(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Boticário", resultado.getContent().getFirst().getNome());
    }

    @Test
    @DisplayName("Deve atualizar fornecedor com sucesso")
    void deveAtualizarFornecedorComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Antigo");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("Nome Novo");

        // When
        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorAtualizado);

        // Then
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Nome Novo", resultado.getNome());
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("Deve listar fornecedores com produtos quando existirem registros")
    void deveListarFornecedoresComProdutosQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Fornecedor fornecedor = new Fornecedor();
        UUID id = UUID.randomUUID();
        fornecedor.setId(id);
        fornecedor.setNome("Boticário");

        List<Fornecedor> fornecedores = List.of(fornecedor);
        Page<Fornecedor> page = new PageImpl<>(fornecedores);
        List<ProdutoResponseDto> produtos = List.of(new ProdutoResponseDto());
        FornecedorComProdutosResponseDto dtoEsperado = new FornecedorComProdutosResponseDto();

        // When
        when(fornecedorRepository.findAll(pageable)).thenReturn(page);
        when(produtoService.listarProdutosPorFornecedor(fornecedor.getId())).thenReturn(produtos);
        when(fornecedorMapper.toFornecedorComProdutosResponseDto(fornecedor, produtos)).thenReturn(dtoEsperado);

        // Then
        Page<FornecedorComProdutosResponseDto> resultado = fornecedorService.listarFornecedoresComProdutos(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        verify(produtoService).listarProdutosPorFornecedor(fornecedor.getId());
        verify(fornecedorMapper).toFornecedorComProdutosResponseDto(fornecedor, produtos);
    }

    @Test
    @DisplayName("Deve excluir fornecedor por ID")
    void deveExcluirFornecedorPorId() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        fornecedorService.excluirPorId(id);

        // Then
        verify(fornecedorRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar fornecedor por ID inexistente")
    void deveLancarExcecaoQuandoBuscarFornecedorPorIdInexistente() {
        // Given
        UUID id = UUID.randomUUID();
        when(fornecedorRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> fornecedorService.buscarPorId(id));
        verify(fornecedorRepository).findById(id);
    }

    @Test
    @DisplayName("Deve cadastrar fornecedor com sucesso")
    void deveCadastrarFornecedorComSucesso() {
        // Given
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Novo Fornecedor");
        fornecedor.setCnpj("12345678901234");
        fornecedor.setDescricao("Descrição do novo fornecedor");
        fornecedor.setImagemUrl("url/nova-imagem.jpg");

        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);

        // When
        Fornecedor resultado = fornecedorService.cadastrarFornecedor(fornecedor);

        // Assert
        assertNotNull(resultado);
        assertEquals(fornecedor.getNome(), resultado.getNome());
        assertEquals(fornecedor.getCnpj(), resultado.getCnpj());
        verify(fornecedorRepository).save(fornecedor);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não existirem fornecedores")
    void deveRetornarPaginaVaziaQuandoNaoExistiremFornecedores() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<Fornecedor> paginaVazia = new PageImpl<>(List.of());
        when(fornecedorRepository.findAll(pageable)).thenReturn(paginaVazia);

        // When
        Page<Fornecedor> resultado = fornecedorService.listar(pageable);

        // Assert
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(fornecedorRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve buscar fornecedor por ID com sucesso")
    void deveBuscarFornecedorPorIdComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678901234");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedor));

        // When
        Fornecedor resultado = fornecedorService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(fornecedor.getNome(), resultado.getNome());
        assertEquals(fornecedor.getCnpj(), resultado.getCnpj());
        verify(fornecedorRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar página vazia ao listar fornecedores com produtos quando não existirem registros")
    void deveRetornarPaginaVaziaAoListarFornecedoresComProdutosQuandoNaoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<Fornecedor> paginaVazia = new PageImpl<>(List.of());
        when(fornecedorRepository.findAll(pageable)).thenReturn(paginaVazia);

        // When
        Page<FornecedorComProdutosResponseDto> resultado = fornecedorService.listarFornecedoresComProdutos(pageable);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(fornecedorRepository).findAll(pageable);
        verifyNoInteractions(produtoService);
        verifyNoInteractions(fornecedorMapper);
    }

    @Test
    @DisplayName("Deve atualizar apenas o nome do fornecedor mantendo outros campos inalterados")
    void deveAtualizarApenasNomeDoFornecedorMantendoOutrosCamposInalterados() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Antigo");
        fornecedorExistente.setCnpj("12345678901234");
        fornecedorExistente.setDescricao("Descrição Original");
        fornecedorExistente.setImagemUrl("url/original.jpg");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("Nome Novo");

        // When
        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorExistente);

        // Then
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Nome Novo", resultado.getNome());
        assertEquals("12345678901234", resultado.getCnpj());
        assertEquals("Descrição Original", resultado.getDescricao());
        assertEquals("url/original.jpg", resultado.getImagemUrl());
        verify(fornecedorRepository).save(fornecedorExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar fornecedor inexistente")
    void deveLancarExcecaoAoTentarAtualizarFornecedorInexistente() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("Nome Novo");

        // When
        when(fornecedorRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> fornecedorService.atualizar(id, fornecedorAtualizado));
        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve manter dados originais quando atualização fornecida for nula")
    void deveManterDadosOriginaisQuandoAtualizacaoFornecidaForNula() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Original");
        fornecedorExistente.setCnpj("12345678901234");

        // When
        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorExistente);

        // Then
        Fornecedor resultado = fornecedorService.atualizar(id, new Fornecedor());

        // Assert
        assertEquals("Nome Original", resultado.getNome());
        assertEquals("12345678901234", resultado.getCnpj());
        verify(fornecedorRepository).save(fornecedorExistente);
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos fornecidos do fornecedor")
    void deveAtualizarApenasOsCamposFornecidosDoFornecedor() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Antigo");
        fornecedorExistente.setCnpj("12345678901234");
        fornecedorExistente.setDescricao("Descrição Antiga");
        fornecedorExistente.setImagemUrl("url/antiga.jpg");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("Nome Novo");
        fornecedorAtualizado.setDescricao("Nova Descrição");

        Fornecedor fornecedorSalvo = new Fornecedor();
        fornecedorSalvo.setId(id);
        fornecedorSalvo.setNome("Nome Novo");
        fornecedorSalvo.setCnpj("12345678901234");
        fornecedorSalvo.setDescricao("Nova Descrição");
        fornecedorSalvo.setImagemUrl("url/antiga.jpg");

        // When
        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorSalvo);

        // Then
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Nome Novo", resultado.getNome());
        assertEquals("12345678901234", resultado.getCnpj());
        assertEquals("Nova Descrição", resultado.getDescricao());
        assertEquals("url/antiga.jpg", resultado.getImagemUrl());
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }


}