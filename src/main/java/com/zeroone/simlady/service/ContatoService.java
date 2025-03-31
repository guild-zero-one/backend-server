package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.cliente.ClienteResponseDto;
import com.zeroone.simlady.dto.contato.ContatoRequestDto;
import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ClienteMapper;
import com.zeroone.simlady.mapper.ContatoMapper;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.repository.ClienteRepository;
import com.zeroone.simlady.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContatoService {

    ContatoMapper contatoMapper;
    ContatoRepository contatoRepository;
    ClienteService clienteService;
    ClienteRepository clienteRepository;

    public ContatoService(ContatoMapper contatoMapper,
                          ContatoRepository contatoRepository,
                          ClienteService clienteService,
                          ClienteRepository clienteRepository) {
        this.contatoMapper = contatoMapper;
        this.contatoRepository = contatoRepository;
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }


    public ContatoResponseDto adicionar(Integer clienteId, ContatoRequestDto dto) {

        Cliente cliente = clienteService.buscarEntidade(clienteId);

        validarCelular(dto.getCelular());
        Contato contato = contatoMapper.toEntity(dto);

        contato.setCliente(cliente);
        cliente.adicionarContato(contato);

        contatoRepository.save(contato);

        return contatoMapper.toDto(contato);

    }

    public ContatoResponseDto buscarPorId(Integer id) {

        Contato contato = buscarEntidade(id);

        return contatoMapper.toDto(contato);

    }

    public Set<ContatoResponseDto> buscarPorCliente (Integer clienteId) {

        Cliente cliente = clienteService.buscarEntidade(clienteId);

        return cliente
                .getContatos()
                .stream()
                .map(contatoMapper::toDto)
                .collect(Collectors.toSet());
    }

    public ContatoResponseDto atualizar(Integer id, ContatoRequestDto dto) {

        Cliente cliente = clienteService.buscarEntidade(id);

        Contato contato = contatoMapper.toEntity(dto);

        if(contatoRepository.existsByCelularAndIdNot(dto.getCelular(), id)) {
            throw new ResourceAlreadyExistsException("Contato já cadastrado.");
        }

        contato.setId(id);
        contato.setCelular(dto.getCelular());
        contato.setCliente(cliente);

        contatoRepository.save(contato);
        return contatoMapper.toDto(contato);
    }

    public void deletar(Integer id) {
        Contato contato = buscarEntidade(id);

        Cliente cliente = clienteService.buscarEntidade(contato.getCliente().getId());
        cliente.getContatos().remove(contato);

        clienteRepository.save(cliente);
        contatoRepository.delete(contato);

    }

    private Contato buscarEntidade(Integer id) {
        return contatoRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contato não encontrado."));
    }
    private void validarCelular(String celular) {
        if(contatoRepository.existsByCelular(celular)) {
            throw new ResourceAlreadyExistsException("Celular já cadastrado.");
        }
    }

}
