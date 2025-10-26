package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto;
import com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AtualizarVendaUseCase {
    private final VendaRepositoryPort repository;

    public AtualizarVendaUseCase(VendaRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<Venda> executar(UUID id, String valorTotal, String desconto, 
                                   LocalDate dataVenda, List<UUID> pedidosIds) {
        Optional<Venda> vendaOpt = repository.buscarPorId(id);
        
        if (vendaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Venda venda = vendaOpt.get();
        
        // Atualizar campos se fornecidos
        if (valorTotal != null) {
            venda = Venda.of(
                    venda.getId(),
                    ValorTotal.of(valorTotal),
                    venda.getDesconto(),
                    venda.getPagamentoRealizado(),
                    venda.getDataVenda(),
                    venda.getPedidosIds(),
                    venda.getCriadoEm(),
                    venda.getAtualizadoEm()
            );
        }
        
        if (desconto != null) {
            venda = Venda.of(
                    venda.getId(),
                    venda.getValorTotal(),
                    Desconto.of(desconto),
                    venda.getPagamentoRealizado(),
                    venda.getDataVenda(),
                    venda.getPedidosIds(),
                    venda.getCriadoEm(),
                    venda.getAtualizadoEm()
            );
        }
        
        if (dataVenda != null) {
            venda = Venda.of(
                    venda.getId(),
                    venda.getValorTotal(),
                    venda.getDesconto(),
                    venda.getPagamentoRealizado(),
                    dataVenda,
                    venda.getPedidosIds(),
                    venda.getCriadoEm(),
                    venda.getAtualizadoEm()
            );
        }
        
        if (pedidosIds != null) {
            // Limpar pedidos existentes e adicionar novos
            for (UUID pedidoId : venda.getPedidosIds()) {
                venda.removerPedido(pedidoId);
            }
            for (UUID pedidoId : pedidosIds) {
                venda.adicionarPedido(pedidoId);
            }
        }
        
        return Optional.of(repository.atualizarVenda(venda));
    }
}
