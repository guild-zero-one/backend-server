package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceAlreadyExistsException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ContatoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContatoService {

    @Autowired
    ContatoMapper contatoMapper;

    @Autowired
    ContatoRepository contatoRepository;

    @Autowired
    ClienteRepository clienteRepository;



    public ContatoDTO adicionarContato(Integer clienteId, Contato contato) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Optional<Contato> possivelContato = contatoRepository.findByCelular(contato.getCelular());

        if(possivelContato.isPresent()) {
            throw new ResourceAlreadyExistsException("Já existe um contato cadastrado com este número de celular.");
        }

        contato.setCliente(cliente);

        cliente.getContatos().add(contato);

        return contatoMapper.toDto(contatoRepository.save(contato));
    }

    public Set<ContatoDTO> buscarContatosPorCliente (Integer clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Set<ContatoDTO> contatosDTO = new HashSet<>();

        contatosDTO.addAll(cliente.getContatos()
                .stream()
                .map(contatoMapper:: toDto)
                .toList());

        return contatosDTO;
    }

    public ContatoDTO atualizarContato(Integer id, ContatoDTO contatoDTO) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        contato.setCelular(contatoDTO.getCelular());

        Contato contatoAtualizado = contatoRepository.save(contato);
        return contatoMapper.toDto(contatoAtualizado);
    }

    public void deletarContato(Integer id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado."));

        Cliente cliente = clienteRepository.findById(contato.getCliente().getId()).get();
        cliente.getContatos().remove(contato);
        clienteRepository.save(cliente);
        contatoRepository.delete(contato);

    }

}
