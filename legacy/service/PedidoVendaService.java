package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoItem;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoVendaService {

    private final PedidoVendaRepository pedidoVendaRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

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
        existente.setStatus(pedidoAtualizado.getStatus());

        Map<Integer, PedidoItem> itensExistentes = existente.getItens().stream()
                .collect(Collectors.toMap(item -> item.getProduto().getId(), item -> item));

        List<PedidoItem> novaLista = new ArrayList<>();

        for (PedidoItem itemAtualizado : pedidoAtualizado.getItens()) {
            Integer idProduto = itemAtualizado.getProduto().getId();

            PedidoItem itemExistente = itensExistentes.get(idProduto);

            if (itemExistente != null) {
                itemExistente.setQuantidade(itemAtualizado.getQuantidade());
                itemExistente.setPrecoUnitario(itemAtualizado.getPrecoUnitario());
                novaLista.add(itemExistente);
            } else {
                Produto produto = produtoRepository.findById(idProduto)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + idProduto));
                PedidoItem novoItem = new PedidoItem();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(itemAtualizado.getQuantidade());
                novoItem.setPrecoUnitario(itemAtualizado.getPrecoUnitario());
                novoItem.setPedidoVenda(existente);
                novaLista.add(novoItem);
            }
        }

        existente.setItens(novaLista);

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
