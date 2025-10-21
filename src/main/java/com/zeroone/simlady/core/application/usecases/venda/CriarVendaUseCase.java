package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.application.usecases.pedido.AlterarStatusPedidoUseCase;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto;
import com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CriarVendaUseCase {
    private final VendaRepositoryPort repository;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;

    public CriarVendaUseCase(VendaRepositoryPort repository, AlterarStatusPedidoUseCase alterarStatusPedidoUseCase) {
        this.repository = repository;
        this.alterarStatusPedidoUseCase = alterarStatusPedidoUseCase;
    }

    public Venda executar(String valorTotal, String desconto, LocalDate dataVenda, List<UUID> pedidosIds) {
        Venda novaVenda = Venda.newVenda(
                ValorTotal.of(valorTotal),
                Desconto.of(desconto),
                dataVenda
        );
        
        if (pedidosIds != null) {
            for (UUID pedidoId : pedidosIds) {
                novaVenda.adicionarPedido(pedidoId);
                alterarStatusPedidoUseCase.executar(pedidoId, StatusPedido.CONCLUIDO);
            }
        }
        
        return repository.salvarVenda(novaVenda);
    }
}
