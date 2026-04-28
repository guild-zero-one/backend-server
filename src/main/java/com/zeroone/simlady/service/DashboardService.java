package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.dashboard.HomeKpisResponseDto;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import com.zeroone.simlady.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final PedidoVendaRepository pedidoVendaRepository;
    private final VendaRepository vendaRepository;

    public HomeKpisResponseDto obterHomeKpis() {
        Long totalItensEstoque = produtoRepository.sumQuantidadeTotalEmEstoque();
        Integer pedidosPendentes = pedidoVendaRepository.countByStatus(StatusPedido.PENDENTE);
        Long vendasPendentes = vendaRepository.countVendasPendentesPagamento();

        HomeKpisResponseDto response = new HomeKpisResponseDto();
        response.setTotalItensEstoque(totalItensEstoque != null ? totalItensEstoque : 0L);
        response.setPedidosPendentes(pedidosPendentes != null ? pedidosPendentes : 0);
        response.setVendasPendentes(vendasPendentes != null ? vendasPendentes : 0L);
        return response;
    }
}
