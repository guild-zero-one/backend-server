package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ContatoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContatoService {

    @Autowired
    ContatoMapper contatoMapper;

    @Autowired
    ContatoRepository contatoRepository;

    @Autowired
    ClienteRepository clienteRepository;



    public ContatoDTO adicionarContato(Integer clienteId, ContatoDTO contatoDTO) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        Contato contato = contatoMapper.toEntity(contatoDTO);
        contato.setClienteId(clienteId);

        cliente.getContatos().add(contato);

        return contatoMapper.toDto(contatoRepository.save(contato));
    }

    public List<ContatoDTO> buscarContatosPorCliente (Integer clienteId) {
        List<ContatoDTO> contatosDto = new ArrayList<>();
        List<Contato> contatos = contatoRepository.findByClienteId(clienteId);

        for (Contato contato : contatos) {
            contatosDto.add(contatoMapper.toDto(contato));
        }

        return contatosDto;



    }

}
