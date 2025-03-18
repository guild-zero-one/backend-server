package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.PedidoVendaDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.PedidoVenda;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Venda;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.VendaRepository;
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
