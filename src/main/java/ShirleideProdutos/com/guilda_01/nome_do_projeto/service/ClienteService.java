package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ClienteMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    public ClienteDTO cadastrarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente.setCriadoEm(LocalDate.now());
        cliente.setAtualizadoEm(LocalDate.now());
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDTO(clienteSalvo);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return clienteMapper.toDTO(cliente);
    }

    public ClienteDTO atualizarCliente(Integer id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(clienteDTO.getNome());
        cliente.setAtualizadoEm(LocalDate.now());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(clienteAtualizado);
    }

    public void deletarCliente(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
}

