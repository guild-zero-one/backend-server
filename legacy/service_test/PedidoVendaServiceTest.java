package com.zeroone.simlady.service_test;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        usuario.setId(1);

        PedidoItem item = new PedidoItem();
        item.setProduto(new com.zeroone.simlady.entity.Produto());
        item.getProduto().setId(2);

        PedidoVenda pedido = new PedidoVenda();
        pedido.setUsuario(usuario);
        pedido.setItens(List.of(item));

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
        when(pedidoVendaRepository.findById(1)).thenReturn(Optional.of(pedido));

        PedidoVenda result = pedidoVendaService.buscar(1);

        assertEquals(pedido, result);
        verify(pedidoVendaRepository).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido de venda inexistente")
    void deveLancarExcecaoAoBuscarPedidoVendaInexistente() {
        when(pedidoVendaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoVendaService.buscar(1));
    }

    @Test
    @DisplayName("Deve atualizar pedido de venda com sucesso")
    void deveAtualizarPedidoVendaComSucesso() {
        PedidoVenda existente = new PedidoVenda();
        existente.setId(1);

        PedidoVenda atualizado = new PedidoVenda();
        atualizado.setUsuario(new Usuario());
        atualizado.setItens(List.of(new PedidoItem()));
        atualizado.setStatus(StatusPedido.CONCLUIDO);

        when(pedidoVendaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pedidoVendaRepository.save(any(PedidoVenda.class))).thenReturn(existente);

        PedidoVenda result = pedidoVendaService.atualizar(1, atualizado);

        assertEquals(existente, result);
        assertEquals(atualizado.getUsuario(), existente.getUsuario());
        assertEquals(atualizado.getItens(), existente.getItens());
        assertEquals(atualizado.getStatus(), existente.getStatus());
        verify(pedidoVendaRepository).save(existente);
    }

    @Test
    @DisplayName("Deve deletar pedido de venda com sucesso")
    void deveDeletarPedidoVendaComSucesso() {
        when(pedidoVendaRepository.existsById(1)).thenReturn(true);

        pedidoVendaService.deletar(1);

        verify(pedidoVendaRepository).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar pedido de venda inexistente")
    void deveLancarExcecaoAoDeletarPedidoVendaInexistente() {
        when(pedidoVendaRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pedidoVendaService.deletar(1));
        verify(pedidoVendaRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deve atualizar status do pedido de venda com sucesso")
    void deveAtualizarStatusDoPedidoVendaComSucesso() {
        PedidoVenda pedido = new PedidoVenda();
        pedido.setStatus(StatusPedido.PENDENTE);

        when(pedidoVendaRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(pedidoVendaRepository.save(any(PedidoVenda.class))).thenReturn(pedido);

        PedidoVenda result = pedidoVendaService.atualizarStatus(1, StatusPedido.CANCELADO);

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
        pedido.setItens(Arrays.asList(item1, item2));

        BigDecimal total = pedidoVendaService.calcularValorTotal(pedido);

        assertEquals(new BigDecimal("36.50"), total);
    }
}