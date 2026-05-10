package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoVendaSearchTest {

    @Mock
    private PedidoVendaRepository pedidoVendaRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoVendaService pedidoVendaService;

    private PedidoVenda pedido;

    @BeforeEach
    void setUp() {
        pedido = new PedidoVenda();
        pedido.setId(UUID.randomUUID());
    }

    @Test
    void testBuscarComFiltro_SearchVazio_RetornaTodasPaginadas() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<PedidoVenda> page = new PageImpl<>(List.of(pedido), pageable, 1);
        when(pedidoVendaRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<PedidoVenda> resultado = pedidoVendaService.buscarComFiltro("", pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        verify(pedidoVendaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testBuscarComFiltro_PorId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String searchId = pedido.getId().toString();
        Page<PedidoVenda> page = new PageImpl<>(List.of(pedido), pageable, 1);

        when(pedidoVendaRepository.findByIdLike(searchId, pageable)).thenReturn(page);

        // Act
        Page<PedidoVenda> resultado = pedidoVendaService.buscarComFiltro(searchId, pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        verify(pedidoVendaRepository, times(1)).findByIdLike(searchId, pageable);
    }

    @Test
    void testBuscarComFiltro_PorNomeUsuario_CaseInsensitive() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String nomeUsuario = "Maria";
        Page<PedidoVenda> page = new PageImpl<>(List.of(pedido), pageable, 1);

        lenient().when(pedidoVendaRepository.findByIdLike(nomeUsuario, pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));
        when(pedidoVendaRepository.findByUsuario_NomeContainingIgnoreCase(nomeUsuario, pageable)).thenReturn(page);

        // Act
        Page<PedidoVenda> resultado = pedidoVendaService.buscarComFiltro(nomeUsuario, pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        verify(pedidoVendaRepository, times(1)).findByUsuario_NomeContainingIgnoreCase(nomeUsuario, pageable);
    }

    @Test
    void testBuscarComFiltro_Paginado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<PedidoVenda> page = new PageImpl<>(List.of(pedido), pageable, 1);
        when(pedidoVendaRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<PedidoVenda> resultado = pedidoVendaService.buscarComFiltro("", pageable);

        // Assert
        assertThat(resultado.getPageable().getPageSize()).isEqualTo(5);
        assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
        verify(pedidoVendaRepository, times(1)).findAll(pageable);
    }
}

