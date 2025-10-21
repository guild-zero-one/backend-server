package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.pedido.Pedido;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PedidoRepositoryPort {
    Pedido salvarPedido(Pedido pedido);
    Optional<Pedido> buscarPorId(UUID id);
    void deletarPorId(UUID id);
    Pedido atualizarPedido(Pedido pedido);
    Page<Pedido> listarTodos(int pagina, int tamanho);
    Page<Pedido> listarPorUsuario(UUID idUsuario, int pagina, int tamanho);
    Page<Pedido> listarPorStatus(String status, int pagina, int tamanho);
    Page<Pedido> listarPorVenda(UUID idVenda, int pagina, int tamanho);
}
