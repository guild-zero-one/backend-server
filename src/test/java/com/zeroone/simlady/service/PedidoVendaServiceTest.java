package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.entity.PedidoItem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoVendaServiceTest {

    @Mock
    private PedidoVendaRepository pedidoVendaRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoVendaService pedidoVendaService;

    @Test
    @DisplayName("Deve cadastrar um novo pedido de venda com sucesso")
    void deveCadastrarPedidoVendaComSucesso() {
        Usuario usuario = new Usuario();

        usuario.setId(UUID.randomUUID());
        PedidoItem item = new PedidoItem();
        item.setProduto(new com.zeroone.simlady.entity.Produto());
        item.getProduto().setId(UUID.randomUUID());

        PedidoVenda pedido = new PedidoVenda();
        pedido.setUsuario(usuario);
        pedido.setItens(Set.of(item));

        when(usuarioService.buscar(usuario.getId())).thenReturn(usuario);
        when(produtoService.buscarPorId(item.getProduto().getId())).thenReturn(item.getProduto());
        when(pedidoVendaRepository.save(any(PedidoVenda.class))).thenReturn(pedido);

        PedidoVenda result = pedidoVendaService.cadastrar(pedido);

        assertEquals(pedido, result);
        verify(usuarioService).buscar(usuario.getId());
        verify(produtoService).buscarPorId(item.getProduto().getId());
        verify(pedidoVendaRepository).save(pedido);
    }

    @Test
    @DisplayName("Deve listar todos os pedidos de venda")
    void deveListarTodosOsPedidosDeVenda() {
        PedidoVenda pedido = new PedidoVenda();
        when(pedidoVendaRepository.findAll()).thenReturn(List.of(pedido));

        List<PedidoVenda> result = pedidoVendaService.listar();

        assertEquals(1, result.size());
        verify(pedidoVendaRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar pedido de venda por ID com sucesso")
    void deveBuscarPedidoVendaPorIdComSucesso() {
        PedidoVenda pedido = new PedidoVenda();
        UUID id = UUID.randomUUID();
        pedido.setId(id);
        when(pedidoVendaRepository.findById(id)).thenReturn(Optional.of(pedido));

        PedidoVenda result = pedidoVendaService.buscar(id);

        assertEquals(pedido, result);
        verify(pedidoVendaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido de venda inexistente")
    void deveLancarExcecaoAoBuscarPedidoVendaInexistente() {
        UUID id = UUID.randomUUID();
        when(pedidoVendaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoVendaService.buscar(id));
    }

    @Test
    @DisplayName("Deve atualizar pedido de venda com sucesso")
    void deveAtualizarPedidoVendaComSucesso() {
        UUID id = UUID.randomUUID();
        PedidoVenda existente = new PedidoVenda();
        existente.setId(id);

        PedidoVenda atualizado = new PedidoVenda();
        atualizado.setUsuario(new Usuario());
        atualizado.setItens(Set.of(new PedidoItem()));
        atualizado.setStatus(StatusPedido.CONCLUIDO);

        when(pedidoVendaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(pedidoVendaRepository.save(any(PedidoVenda.class))).thenReturn(existente);

        PedidoVenda result = pedidoVendaService.atualizar(id, atualizado);

        assertEquals(existente, result);
        assertEquals(atualizado.getUsuario(), existente.getUsuario());
        assertEquals(atualizado.getItens(), existente.getItens());
        assertEquals(atualizado.getStatus(), existente.getStatus());
        verify(pedidoVendaRepository).save(existente);
    }

    @Test
    @DisplayName("Deve deletar pedido de venda com sucesso")
    void deveDeletarPedidoVendaComSucesso() {
        UUID id = UUID.randomUUID();
        when(pedidoVendaRepository.existsById(id)).thenReturn(true);

        pedidoVendaService.deletar(id);

        verify(pedidoVendaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar pedido de venda inexistente")
    void deveLancarExcecaoAoDeletarPedidoVendaInexistente() {
        UUID id = UUID.randomUUID();
        when(pedidoVendaRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pedidoVendaService.deletar(id));
        verify(pedidoVendaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve atualizar status do pedido de venda com sucesso")
    void deveAtualizarStatusDoPedidoVendaComSucesso() {
        UUID id = UUID.randomUUID();
        PedidoVenda pedido = new PedidoVenda();
        pedido.setId(id);
        pedido.setStatus(StatusPedido.PENDENTE);

        when(pedidoVendaRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoVendaRepository.save(any(PedidoVenda.class))).thenReturn(pedido);

        PedidoVenda result = pedidoVendaService.atualizarStatus(id, StatusPedido.CANCELADO);

        assertEquals(StatusPedido.CANCELADO, result.getStatus());
        verify(pedidoVendaRepository).save(pedido);
    }

    @Test
    @DisplayName("Deve calcular o valor total do pedido de venda corretamente")
    void deveCalcularValorTotalDoPedidoVendaCorretamente() {
        PedidoItem item1 = new PedidoItem();
        item1.setPrecoUnitario(new BigDecimal("10.00"));
        item1.setQuantidade(2);

        PedidoItem item2 = new PedidoItem();
        item2.setPrecoUnitario(new BigDecimal("5.50"));
        item2.setQuantidade(3);

        PedidoVenda pedido = new PedidoVenda();
        pedido.setItens(Set.of(item1, item2));

        BigDecimal total = pedidoVendaService.calcularValorTotal(pedido);

        assertEquals(new BigDecimal("36.50"), total);
    }
}