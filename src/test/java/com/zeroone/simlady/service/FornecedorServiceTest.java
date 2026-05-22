package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.repository.FornecedorRepository;
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
    private ProdutoRepository produtoRepository;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @Mock
    private ProdutoMapper produtoMapper;

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

        when(fornecedorRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Fornecedor> resultado = fornecedorService.listar(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Boticário", resultado.getContent().getFirst().getNome());
    }

    @Test
    @DisplayName("Deve cadastrar fornecedor com sucesso preservando o caso original do nome")
    void deveCadastrarFornecedorComSucesso() {
        // Given
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Novo Fornecedor");
        fornecedor.setCnpj("12345678901234");
        fornecedor.setDescricao("Descrição do novo fornecedor");
        fornecedor.setImagemUrl("url/nova-imagem.jpg");

        when(fornecedorRepository.findByNomeIgnoreCase("Novo Fornecedor")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Fornecedor resultado = fornecedorService.cadastrarFornecedor(fornecedor);

        // Assert
        assertNotNull(resultado);
        assertEquals("Novo Fornecedor", resultado.getNome());
        assertEquals(fornecedor.getCnpj(), resultado.getCnpj());
        verify(fornecedorRepository).save(fornecedor);
    }

    @Test
    @DisplayName("Deve preservar o caso original do nome ao cadastrar fornecedor")
    void deveCadastrarFornecedorPreservandoCasoOriginalDoNome() {
        // Given
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("  O Boticário  ");

        when(fornecedorRepository.findByNomeIgnoreCase("O Boticário")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Fornecedor resultado = fornecedorService.cadastrarFornecedor(fornecedor);

        // Assert
        assertEquals("O Boticário", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar fornecedor com nome duplicado")
    void deveLancarExcecaoAoCadastrarFornecedorComNomeDuplicado() {
        // Given
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Existente");

        Fornecedor duplicado = new Fornecedor();
        duplicado.setNome("Fornecedor Existente");

        when(fornecedorRepository.findByNomeIgnoreCase("Fornecedor Existente")).thenReturn(Optional.of(duplicado));

        // Then & Assert
        assertThrows(ResourceAlreadyExistsException.class,
                () -> fornecedorService.cadastrarFornecedor(fornecedor));
        verify(fornecedorRepository, never()).save(any());
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

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.findByNomeIgnoreCase("Nome Novo")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Nome Novo", resultado.getNome());
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("Deve preservar o caso original do nome ao atualizar fornecedor")
    void deveAtualizarFornecedorPreservandoCasoOriginalDoNome() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Antigo");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("  Boticário Premium  ");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.findByNomeIgnoreCase("Boticário Premium")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Boticário Premium", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar fornecedor com nome já existente")
    void deveLancarExcecaoAoAtualizarFornecedorComNomeJaExistente() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Nome Antigo");

        Fornecedor outroFornecedor = new Fornecedor();
        outroFornecedor.setId(UUID.randomUUID());
        outroFornecedor.setNome("Nome Duplicado");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("Nome Duplicado");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.findByNomeIgnoreCase("Nome Duplicado")).thenReturn(Optional.of(outroFornecedor));

        // Then & Assert
        assertThrows(ResourceAlreadyExistsException.class,
                () -> fornecedorService.atualizar(id, fornecedorAtualizado));
        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve não verificar duplicata ao atualizar com o mesmo nome (case insensitive)")
    void deveNaoVerificarDuplicataAoAtualizarComMesmoNome() {
        // Given
        UUID id = UUID.randomUUID();
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Boticário");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setNome("boticário");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert: não deve consultar repositório para checar duplicata
        verify(fornecedorRepository, never()).findByNomeIgnoreCase(any());
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

        Produto produto = new Produto();
        ProdutoResponseDto produtoDto = new ProdutoResponseDto();
        FornecedorComProdutosResponseDto dtoEsperado = new FornecedorComProdutosResponseDto();

        List<Fornecedor> fornecedores = List.of(fornecedor);
        Page<Fornecedor> page = new PageImpl<>(fornecedores);

        when(fornecedorRepository.findAll(pageable)).thenReturn(page);
        when(produtoRepository.findByFornecedorId(id)).thenReturn(List.of(produto));
        when(produtoMapper.toResponseDto(produto)).thenReturn(produtoDto);
        when(fornecedorMapper.toFornecedorComProdutosResponseDto(fornecedor, List.of(produtoDto))).thenReturn(dtoEsperado);

        // When
        Page<FornecedorComProdutosResponseDto> resultado = fornecedorService.listarFornecedoresComProdutos(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        verify(produtoRepository).findByFornecedorId(id);
        verify(produtoMapper).toResponseDto(produto);
        verify(fornecedorMapper).toFornecedorComProdutosResponseDto(fornecedor, List.of(produtoDto));
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
        verifyNoInteractions(produtoRepository);
        verifyNoInteractions(produtoMapper);
        verifyNoInteractions(fornecedorMapper);
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
        assertEquals("Fornecedor Teste", resultado.getNome());
        assertEquals(fornecedor.getCnpj(), resultado.getCnpj());
        verify(fornecedorRepository).findById(id);
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

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.findByNomeIgnoreCase("Nome Novo")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
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

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
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

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.findByNomeIgnoreCase("Nome Novo")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Fornecedor resultado = fornecedorService.atualizar(id, fornecedorAtualizado);

        // Assert
        assertEquals("Nome Novo", resultado.getNome());
        assertEquals("12345678901234", resultado.getCnpj());
        assertEquals("Nova Descrição", resultado.getDescricao());
        assertEquals("url/antiga.jpg", resultado.getImagemUrl());
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }
}
