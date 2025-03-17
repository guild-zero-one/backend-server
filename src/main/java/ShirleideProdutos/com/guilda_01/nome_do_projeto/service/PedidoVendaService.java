package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.PedidoVendaDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.PedidoVendaMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.PedidoVenda;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.PedidoVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PedidoVendaService {

    @Autowired
    PedidoVendaRepository pedidoVendaRepository;

    @Autowired
    PedidoVendaMapper pedidoVendaMapper;

    public PedidoVendaDTO cadastrar(PedidoVenda pedidoVenda) {

        pedidoVenda.setId(null);
        pedidoVenda.setStatus("PENDENTE");

        pedidoVendaRepository.save(pedidoVenda);

        return pedidoVendaMapper.toDto(pedidoVenda);

    }

}
