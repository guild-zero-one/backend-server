package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.VendaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final PedidoVendaRepository pedidoVendaRepository;
    private final PedidoVendaService pedidoVendaService;

    @Transactional
    public Venda cadastrar(Venda venda, List<UUID> idPedidos) {
        List<PedidoVenda> pedidos = pedidoVendaRepository.findAllById(idPedidos);

        if(pedidos.isEmpty()) {
            throw new ResourceNotFoundException("Pedidos não encontrados");
        }

        pedidos.forEach(pedido -> {pedido.setVenda(venda);});
        venda.setPedidos(pedidos);
        venda.setValorTotal(calcularTotal(venda).subtract(venda.getDesconto()));

        return vendaRepository.save(venda);
    }

    public BigDecimal calcularTotal(Venda venda) {
        return venda
                .getPedidos()
                .stream()
                .map(pedidoVendaService::calcularValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Venda> listar() {
        return vendaRepository.findAll();
    }

    public Page<Venda> listar(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    public Venda buscar(UUID id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }

    public void deletar(UUID id) {
        if (!vendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venda não encontrada");
        }
        vendaRepository.deleteById(id);
    }
}
