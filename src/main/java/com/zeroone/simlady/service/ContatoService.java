package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.ContatoDTO;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ContatoMapper;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.repository.ClienteRepository;
import com.zeroone.simlady.repository.ContatoRepository;
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
        contato.setId(null);
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

    public ContatoDTO buscarContatoPorId(Integer id) {

        if(!contatoRepository.findById(id).isPresent()) {
            throw  new ResourceNotFoundException("Contato não encontrado.");
        }

        return contatoMapper.toDto(contatoRepository.findById(id).get());

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

    public ContatoDTO atualizarContato(Integer id, Contato contato) {
        Contato contatoEncontrado = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        contatoEncontrado.setCelular(contato.getCelular());

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
