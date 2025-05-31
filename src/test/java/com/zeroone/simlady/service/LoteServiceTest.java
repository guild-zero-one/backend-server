package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.LoteItemRepository;
import com.zeroone.simlady.repository.LoteRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private LoteItemRepository loteItemRepository;

    @InjectMocks
    private LoteService loteService;

    @Test
    @DisplayName("Deve listar lotes quando existirem registros")
    void deveListarLotesQuandoExistiremRegistros() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Lote lote = new Lote();
        lote.setId(1);
        lote.setQtdLote(10);
        lote.setValorTotal(100.0);
        lote.setCriadoEm(LocalDateTime.now());
        lote.setAtualizadoEm(LocalDateTime.now());

        List<Lote> lotes = List.of(lote);
        Page<Lote> page = new PageImpl<>(lotes);

        // When
        when(loteRepository.findAll(pageable)).thenReturn(page);

        // Then
        Page<Lote> resultado = loteService.listar(pageable);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals(10, resultado.getContent().get(0).getQtdLote());
    }

    @Test
    @DisplayName("Deve cadastrar lote com sucesso")
    void deveCadastrarLoteComSucesso() {
        // Given
        Lote lote = new Lote();
        lote.setQtdLote(5);
        lote.setValorTotal(50.0);

        when(loteRepository.save(any(Lote.class))).thenReturn(lote);

        // When
        Lote resultado = loteService.cadastrarLote(lote);

        // Assert
        assertNotNull(resultado);
        assertEquals(5, resultado.getQtdLote());
        assertEquals(50.0, resultado.getValorTotal());
        verify(loteRepository).save(lote);
    }

    @Test
    @DisplayName("Deve buscar lote por ID com sucesso")
    void deveBuscarLotePorIdComSucesso() {
        // Given
        Integer id = 1;
        Lote lote = new Lote();
        lote.setId(id);
        lote.setQtdLote(10);
        lote.setValorTotal(100.0);

        when(loteRepository.findById(id)).thenReturn(Optional.of(lote));

        // When
        Lote resultado = loteService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(10, resultado.getQtdLote());
        assertEquals(100.0, resultado.getValorTotal());
        verify(loteRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar lote por ID inexistente")
    void deveLancarExcecaoQuandoBuscarLotePorIdInexistente() {
        // Given
        Integer id = 999;
        when(loteRepository.findById(id)).thenReturn(Optional.empty());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class, () -> loteService.buscarPorId(id));
        verify(loteRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar lote com sucesso")
    void deveAtualizarLoteComSucesso() {
        // Given
        Integer id = 1;
        Lote loteExistente = new Lote();
        loteExistente.setId(id);
        loteExistente.setQtdLote(10);
        loteExistente.setValorTotal(100.0);

        Lote loteAtualizado = new Lote();
        loteAtualizado.setQtdLote(15);
        loteAtualizado.setValorTotal(150.0);

        when(loteRepository.findById(id)).thenReturn(Optional.of(loteExistente));
        when(loteRepository.save(any(Lote.class))).thenReturn(loteAtualizado);

        // When
        Lote resultado = loteService.atualizarLote(id, loteAtualizado);

        // Assert
        assertEquals(15, resultado.getQtdLote());
        assertEquals(150.0, resultado.getValorTotal());
        verify(loteRepository).save(any(Lote.class));
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não existirem lotes")
    void deveRetornarPaginaVaziaQuandoNaoExistiremLotes() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<Lote> paginaVazia = new PageImpl<>(List.of());
        when(loteRepository.findAll(pageable)).thenReturn(paginaVazia);

        // When
        Page<Lote> resultado = loteService.listar(pageable);

        // Assert
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(loteRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve deletar lote por ID")
    void deveDeletarLotePorId() {
        // Given
        Integer id = 1;
        Lote lote = new Lote();
        lote.setId(id);

        when(loteRepository.findById(id)).thenReturn(Optional.of(lote));

        // When
        loteService.deletarLote(id);

        // Then
        verify(loteRepository).delete(lote);
    }

    @Test
    @DisplayName("Deve cadastrar itens do lote com sucesso")
    void deveCadastrarItensDoLoteComSucesso() {
        // Given
        List<LoteItem> loteItems = List.of(new LoteItem());
        when(loteItemRepository.saveAll(any())).thenReturn(loteItems);

        // When
        List<LoteItem> resultado = loteService.cadastrarLoteItem(loteItems);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(loteItemRepository).saveAll(loteItems);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar itens de lote inexistente")
    void deveLancarExcecaoAoTentarAtualizarItensLoteInexistente() {
        // Given
        Integer id = 999;
        List<LoteItem> loteItems = List.of(new LoteItem());
        when(loteItemRepository.findAllByLoteId(id)).thenReturn(List.of());

        // Then & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> loteService.atualizarLoteItem(id, loteItems));
        verify(loteItemRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Deve atualizar apenas a quantidade do lote mantendo valor total inalterado")
    void deveAtualizarApenasQuantidadeLoteMantendoValorTotalInalterado() {
        // Given
        Integer id = 1;
        Lote loteExistente = new Lote();
        loteExistente.setId(id);
        loteExistente.setQtdLote(10);
        loteExistente.setValorTotal(100.0);

        Lote loteAtualizado = new Lote();
        loteAtualizado.setQtdLote(15);

        Lote loteSalvo = new Lote();
        loteSalvo.setId(id);
        loteSalvo.setQtdLote(15);
        loteSalvo.setValorTotal(100.0);

        // When
        when(loteRepository.findById(id)).thenReturn(Optional.of(loteExistente));
        when(loteRepository.save(any(Lote.class))).thenReturn(loteSalvo);

        // Then
        Lote resultado = loteService.atualizarLote(id, loteAtualizado);

        // Assert
        assertEquals(15, resultado.getQtdLote());
        assertEquals(100.0, resultado.getValorTotal());
        verify(loteRepository).save(any(Lote.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas o valor total mantendo quantidade inalterada")
    void deveAtualizarApenasValorTotalMantendoQuantidadeInalterada() {
        // Given
        Integer id = 1;
        Lote loteExistente = new Lote();
        loteExistente.setId(id);
        loteExistente.setQtdLote(10);
        loteExistente.setValorTotal(100.0);

        Lote loteAtualizado = new Lote();
        loteAtualizado.setValorTotal(150.0);

        Lote loteSalvo = new Lote();
        loteSalvo.setId(id);
        loteSalvo.setQtdLote(10);
        loteSalvo.setValorTotal(150.0);

        // When
        when(loteRepository.findById(id)).thenReturn(Optional.of(loteExistente));
        when(loteRepository.save(any(Lote.class))).thenReturn(loteSalvo);

        // Then
        Lote resultado = loteService.atualizarLote(id, loteAtualizado);

        // Assert
        assertEquals(10, resultado.getQtdLote());
        assertEquals(150.0, resultado.getValorTotal());
        verify(loteRepository).save(any(Lote.class));
    }

    @Test
    @DisplayName("Deve manter dados originais quando atualização do lote for nula")
    void deveManterDadosOriginaisQuandoAtualizacaoLoteForNula() {
        // Given
        Integer id = 1;
        Lote loteExistente = new Lote();
        loteExistente.setId(id);
        loteExistente.setQtdLote(10);
        loteExistente.setValorTotal(100.0);

        // When
        when(loteRepository.findById(id)).thenReturn(Optional.of(loteExistente));
        when(loteRepository.save(any(Lote.class))).thenReturn(loteExistente);

        // Then
        Lote resultado = loteService.atualizarLote(id, new Lote());

        // Assert
        assertEquals(10, resultado.getQtdLote());
        assertEquals(100.0, resultado.getValorTotal());
        verify(loteRepository).save(any(Lote.class));
    }

    @Test
    @DisplayName("Deve atualizar loteItems mantendo dados do lote inalterados")
    void deveAtualizarLoteItemsMantendoDadosLoteInalterados() {
        // Given
        Integer id = 1;

        Lote lote = new Lote();
        lote.setId(id);

        Produto produto = new Produto();
        produto.setId(1);

        LoteItem itemExistente = new LoteItem();
        itemExistente.setId(1);
        itemExistente.setLote(lote);
        itemExistente.setQtdLoteCompra(10);
        itemExistente.setValorUnitarioCompra(100.0);
        itemExistente.setProduto(produto);
        itemExistente.setDataValidade(LocalDate.now());

        LoteItem itemAtualizado = new LoteItem();
        itemAtualizado.setId(1); // Mesmo ID do item existente
        itemAtualizado.setQtdLoteCompra(15);
        itemAtualizado.setLote(lote);

        List<LoteItem> loteItemsExistentes = List.of(itemExistente);
        List<LoteItem> loteItemsAtualizados = List.of(itemAtualizado);

        // When
        when(loteItemRepository.findAllByLoteId(id)).thenReturn(loteItemsExistentes);
        when(loteItemRepository.save(any(LoteItem.class))).thenReturn(itemExistente);

        // Then
        List<LoteItem> resultado = loteService.atualizarLoteItem(id, loteItemsAtualizados);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(15, resultado.get(0).getQtdLoteCompra());
        assertEquals(100.0, resultado.get(0).getValorUnitarioCompra());
        assertEquals(lote.getId(), resultado.get(0).getLote().getId());
        verify(loteItemRepository).save(any(LoteItem.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos fornecidos do LoteItem")
    void deveAtualizarApenasOsCamposFornecidosDoLoteItem() {
        // Given
        Integer loteId = 1;

        Lote lote = new Lote();
        lote.setId(loteId);

        Produto produto = new Produto();
        produto.setId(1);

        LoteItem itemExistente = new LoteItem();
        itemExistente.setId(1);
        itemExistente.setLote(lote);
        itemExistente.setQtdLoteCompra(10);
        itemExistente.setValorUnitarioCompra(100.0);
        itemExistente.setProduto(produto);
        itemExistente.setDataValidade(LocalDate.now());

        LoteItem itemAtualizado = new LoteItem();
        itemAtualizado.setId(1);
        itemAtualizado.setQtdLoteCompra(15);

        List<LoteItem> itensExistentes = List.of(itemExistente);
        List<LoteItem> itensAtualizados = List.of(itemAtualizado);

        // When
        when(loteItemRepository.findAllByLoteId(loteId)).thenReturn(itensExistentes);
        when(loteItemRepository.save(any(LoteItem.class))).thenReturn(itemExistente);

        // Then
        List<LoteItem> resultado = loteService.atualizarLoteItem(loteId, itensAtualizados);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(15, resultado.get(0).getQtdLoteCompra());
        assertEquals(100.0, resultado.get(0).getValorUnitarioCompra());
        assertEquals(produto.getId(), resultado.get(0).getProduto().getId());
        assertEquals(lote.getId(), resultado.get(0).getLote().getId());
        assertNotNull(resultado.get(0).getDataValidade());
        verify(loteItemRepository).save(any(LoteItem.class));
    }
}