package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.repository.PedidoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoItemRepository pedidoItemRepository;

    public List<ProdutosMaisVendidosResponseDto> listarVendasPorProduto() {
        return pedidoItemRepository.buscarProdutosMaisVendidos();
    }
}
