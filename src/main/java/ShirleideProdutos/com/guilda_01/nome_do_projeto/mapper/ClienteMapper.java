package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCriadoEm(), cliente.getAtualizadoEm(), cliente.getContatos());
    }

    public Cliente toEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.getId());
        cliente.setNome(clienteDTO.getNome());
        cliente.setCriadoEm(clienteDTO.getCriadoEm());
        cliente.setAtualizadoEm(clienteDTO.getAtualizadoEm());
        return cliente;
    }
}

