package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.PedidoVendaDTO;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.repository.ClienteRepository;
import com.zeroone.simlady.repository.VendaRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoVendaMapper {

    VendaRepository vendaRepository;
    ClienteRepository clienteRepository;

    public PedidoVendaDTO toDto(PedidoVenda pedidoVenda) {

        Integer idVenda = -1;

        if(pedidoVenda.getVenda() != null) {
            idVenda = pedidoVenda.getVenda().getId();
        }


        return new PedidoVendaDTO(
                pedidoVenda.getId(),
                pedidoVenda.getStatus(),
                idVenda,
                pedidoVenda.getCliente().getId(),
                pedidoVenda.getCriadoEm(),
                pedidoVenda.getAtualizadoEm()
        );
    }

    public PedidoVenda toEntity(PedidoVendaDTO pedidoVendaDTO) {
        PedidoVenda pedidoVenda = new PedidoVenda();
        pedidoVenda.setId(pedidoVendaDTO.getId());
        pedidoVenda.setStatus(pedidoVendaDTO.getStatus());

        Venda venda = vendaRepository.findById(pedidoVendaDTO.getIdVenda())
                .orElse(null);
        Cliente cliente = clienteRepository.findById(pedidoVendaDTO.getIdCliente())
                .orElse(null);

        pedidoVenda.setVenda(venda);
        pedidoVenda.setCliente(cliente);

        pedidoVenda.setCriadoEm(pedidoVendaDTO.getCriadoEm());
        pedidoVenda.setAtualizadoEm(pedidoVendaDTO.getAtualizadoEm());

        return pedidoVenda;
    }
}
