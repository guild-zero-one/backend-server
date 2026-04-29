package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.VendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private PedidoVendaRepository pedidoVendaRepository;
    @Mock
    private PedidoVendaService pedidoVendaService;

    @InjectMocks
    private VendaService vendaService;

    @Test
    @DisplayName("Deve cadastrar uma venda com sucesso")
    void deveCadastrarVendaComSucesso() {
        Venda venda = new Venda();
        PedidoVenda pedido1 = new PedidoVenda();
        PedidoVenda pedido2 = new PedidoVenda();
        List<PedidoVenda> pedidos = Arrays.asList(pedido1, pedido2);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = Arrays.asList(id1, id2);

        when(pedidoVendaRepository.findAllById(ids)).thenReturn(pedidos);
        when(pedidoVendaService.calcularValorTotal(pedido1)).thenReturn(new BigDecimal("10.00"));
        when(pedidoVendaService.calcularValorTotal(pedido2)).thenReturn(new BigDecimal("20.00"));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = vendaService.cadastrar(venda, ids);

        assertEquals(new java.util.HashSet<>(pedidos), resultado.getPedidos());
        assertEquals(new BigDecimal("30.00"), resultado.getValorTotal());
        verify(vendaRepository).save(venda);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar venda sem pedidos encontrados")
    void deveLancarExcecaoAoCadastrarVendaSemPedidos() {
        Venda venda = new Venda();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = Arrays.asList(id1, id2);

        when(pedidoVendaRepository.findAllById(ids)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> vendaService.cadastrar(venda, ids));
    }

    @Test
    @DisplayName("Deve calcular o valor total corretamente")
    void deveCalcularValorTotalCorretamente() {
        Venda venda = new Venda();
        PedidoVenda pedido1 = new PedidoVenda();
        PedidoVenda pedido2 = new PedidoVenda();
        venda.setPedidos(new java.util.HashSet<>(Arrays.asList(pedido1, pedido2)));

        when(pedidoVendaService.calcularValorTotal(pedido1)).thenReturn(new BigDecimal("5.00"));
        when(pedidoVendaService.calcularValorTotal(pedido2)).thenReturn(new BigDecimal("7.50"));

        BigDecimal total = vendaService.calcularTotal(venda);

        assertEquals(new BigDecimal("12.50"), total);
    }

    @Test
    @DisplayName("Deve listar todas as vendas")
    void deveListarTodasAsVendas() {
        Venda venda = new Venda();
        when(vendaRepository.findAll()).thenReturn(List.of(venda));

        List<Venda> resultado = vendaService.listar();

        assertEquals(1, resultado.size());
        verify(vendaRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar venda por ID com sucesso")
    void deveBuscarVendaPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        Venda venda = new Venda();
        when(vendaRepository.findById(id)).thenReturn(Optional.of(venda));

        Venda resultado = vendaService.buscar(id);

        assertEquals(venda, resultado);
        verify(vendaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar venda inexistente")
    void deveLancarExcecaoAoBuscarVendaInexistente() {
        UUID id = UUID.randomUUID();
        when(vendaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vendaService.buscar(id));
    }

    @Test
    @DisplayName("Deve deletar venda com sucesso")
    void deveDeletarVendaComSucesso() {
        UUID id = UUID.randomUUID();
        when(vendaRepository.existsById(id)).thenReturn(true);

        vendaService.deletar(id);

        verify(vendaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar venda inexistente")
    void deveLancarExcecaoAoDeletarVendaInexistente() {
        UUID id = UUID.randomUUID();
        when(vendaRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> vendaService.deletar(id));
        verify(vendaRepository, never()).deleteById(any(UUID.class));
    }
}