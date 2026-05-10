package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.venda.VendaComUsuarioDto;
import com.zeroone.simlady.dto.venda.VendaDetalheDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.PedidoDetalheVendaMapper;
import com.zeroone.simlady.mapper.UsuarioResumoVendaMapper;
import com.zeroone.simlady.mapper.VendaComUsuarioMapper;
import com.zeroone.simlady.mapper.VendaDetalheMapper;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import com.zeroone.simlady.repository.VendaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final PedidoVendaRepository pedidoVendaRepository;
    private final PedidoVendaService pedidoVendaService;
    private final VendaDetalheMapper vendaDetalheMapper;
    private final VendaComUsuarioMapper vendaComUsuarioMapper;
    private final PedidoDetalheVendaMapper pedidoDetalheVendaMapper;
    private final UsuarioResumoVendaMapper usuarioResumoVendaMapper;

    @Transactional
    public Venda cadastrar(Venda venda, List<UUID> idPedidos) {
        List<PedidoVenda> pedidos = pedidoVendaRepository.findAllById(idPedidos);

        if(pedidos.isEmpty()) {
            throw new ResourceNotFoundException("Pedidos não encontrados");
        }

        pedidos.forEach(pedido -> {pedido.setVenda(venda);});
        venda.setPedidos(new HashSet<>(pedidos));
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

    public Page<VendaComUsuarioDto> listar(Pageable pageable) {
        return vendaRepository.findAll(pageable).map(venda -> {
            VendaComUsuarioDto dto = vendaComUsuarioMapper.toDto(venda);
            if (venda.getPedidos() != null && !venda.getPedidos().isEmpty()) {
                dto.setUsuario(usuarioResumoVendaMapper.toDto(new ArrayList<>(venda.getPedidos()).get(0).getUsuario()));
            }
            return dto;
        });
    }

    public Venda buscar(UUID id) {
        return vendaRepository.buscarDetalhePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }

    public VendaDetalheDto buscarDetalhe(UUID id) {
        Venda venda = buscar(id);
        VendaDetalheDto dto = vendaDetalheMapper.toDto(venda);
        if (venda.getPedidos() != null && !venda.getPedidos().isEmpty()) {
            dto.setPedido(pedidoDetalheVendaMapper.toDto(new ArrayList<>(venda.getPedidos()).get(0)));
        }
        return dto;
    }

    @Transactional
    public VendaDetalheDto atualizarPagamento(UUID id, Boolean pagamentoRealizado) {
        Venda venda = buscar(id);
        venda.setPagamentoRealizado(pagamentoRealizado);
        vendaRepository.save(venda);
        return buscarDetalhe(id);
    }

    public void deletar(UUID id) {
        if (!vendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venda não encontrada");
        }
        vendaRepository.deleteById(id);
    }
}
