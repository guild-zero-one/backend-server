package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.PedidoVendaDTO;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.PedidoVendaMapper;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.repository.ClienteRepository;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoVendaService {

    @Autowired
    PedidoVendaRepository pedidoVendaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PedidoVendaMapper pedidoVendaMapper;

    public PedidoVendaDTO cadastrar(PedidoVenda pedidoVenda, Integer clienteId) {

        buscarCliente(clienteId);

        pedidoVenda.setCliente(clienteRepository.findById(clienteId).get());
        pedidoVenda.setId(null);
        pedidoVenda.setStatus("PENDENTE");

        pedidoVendaRepository.save(pedidoVenda);

        return pedidoVendaMapper.toDto(pedidoVenda);

    }

    public List<PedidoVendaDTO> listar() {
        List<PedidoVenda> pedidoVendas = pedidoVendaRepository.findAll();

        return pedidoVendas
                .stream()
                .map(pedidoVendaMapper::toDto)
                .toList();
    }

    public PedidoVendaDTO buscarPorId(Integer id) {
        buscarPedidoVendaPorId(id);

        return pedidoVendaMapper.toDto(pedidoVendaRepository.findById(id).get());
    }

    public List<PedidoVendaDTO> buscarPorCliente(Integer id) {
        Cliente cliente = buscarCliente(id);

        List<PedidoVenda> pedidos = pedidoVendaRepository.findAllByCliente(cliente);

        return pedidos
                .stream()
                .map(pedidoVendaMapper::toDto)
                .toList();
    }

    public PedidoVendaDTO atualizar(Integer id, PedidoVenda pedidoVenda) {
        pedidoVenda.setId(id);

        PedidoVenda antigo = buscarPedidoVendaPorId(id);
        pedidoVenda.setCliente(antigo.getCliente());
        pedidoVenda.setCriadoEm(antigo.getCriadoEm());

        return pedidoVendaMapper.toDto(pedidoVendaRepository.save(pedidoVenda));

    }

    public PedidoVendaDTO atualizarStatus(Integer id, String status) {
        buscarPedidoVendaPorId(id);

        PedidoVenda pedidoVenda = pedidoVendaRepository.findById(id).get();
        pedidoVenda.setStatus(status);

        return pedidoVendaMapper.toDto(pedidoVendaRepository.save(pedidoVenda));
    }

    public void remover(Integer id) {
        buscarPedidoVendaPorId(id);
        pedidoVendaRepository.deleteById(id);
    }

    public List<PedidoVendaDTO> buscarTodosPorStatus(String status)  {

        List<PedidoVenda> pedidos = pedidoVendaRepository.findAllByStatus(status);

        return pedidos
                .stream()
                .map(pedidoVendaMapper:: toDto)
                .toList();

    }



    private PedidoVenda buscarPedidoVendaPorId(Integer id) {
        return pedidoVendaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    private Cliente buscarCliente(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }
}
