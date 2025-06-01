package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoVendaService {

    private final PedidoVendaRepository pedidoVendaRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;

    public PedidoVenda cadastrar(PedidoVenda pedido) {
        Integer idUsuario = pedido.getUsuario().getId();

        usuarioService.buscar(idUsuario);
        pedido.getItens().forEach(item -> item.setPedidoVenda(pedido));

        pedido.getItens().forEach(item -> item.setProduto(produtoService.buscarPorId(item.getProduto().getId())));

        return pedidoVendaRepository.save(pedido);
    }

    public List<PedidoVenda> listar() {
        return pedidoVendaRepository.findAll();
    }

    public PedidoVenda buscar(Integer id) {
        return pedidoVendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    public PedidoVenda atualizar(Integer id, PedidoVenda pedidoAtualizado) {
        PedidoVenda existente = buscar(id);

        existente.setUsuario(pedidoAtualizado.getUsuario());
        existente.setItens(pedidoAtualizado.getItens());
        existente.getItens().forEach(item -> item.setPedidoVenda(existente));

        existente.setStatus(pedidoAtualizado.getStatus());

        return pedidoVendaRepository.save(existente);
    }

    public void deletar(Integer id) {
        if (!pedidoVendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        pedidoVendaRepository.deleteById(id);
    }

    public PedidoVenda atualizarStatus(Integer id, StatusPedido novoStatus) {
        PedidoVenda pedido = buscar(id);
        pedido.setStatus(novoStatus);
        return pedidoVendaRepository.save(pedido);
    }

    public BigDecimal calcularValorTotal(PedidoVenda pedido) {
        return pedido
                .getItens()
                .stream()
                .map(item -> item.getPrecoUnitario()
                        .multiply(BigDecimal
                                .valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
