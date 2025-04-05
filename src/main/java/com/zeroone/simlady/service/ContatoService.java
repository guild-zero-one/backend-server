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

@RequiredArgsConstructor
@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;



    public Contato adicionar(Integer clienteId, Contato contato) {

        Cliente cliente = clienteService.buscar(clienteId);

        validarCelular(contato.getCelular());

        contato.setCliente(cliente);
        cliente.adicionarContato(contato);

        contatoRepository.save(contato);

        return contato;

    }

    public Contato buscar(Integer id) {
        return contatoRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contato não encontrado."));
    }

    public Set<Contato> buscarPorCliente (Integer clienteId) {

        Cliente cliente = clienteService.buscar(clienteId);

        return cliente
                .getContatos();
    }

    public Contato atualizar(Integer id, Contato contato) {

        Cliente cliente = clienteService.buscar(id);


        if(contatoRepository.existsByCelularAndIdNot(contato.getCelular(), id)) {
            throw new ResourceAlreadyExistsException("Celular já cadastrado.");
        }

        contato.setId(id);
        contato.setCliente(cliente);

        contatoRepository.save(contato);
        return contato;
    }

    public void deletar(Integer id) {
        Contato contato = buscar(id);

        Cliente cliente = clienteService.buscar(contato.getCliente().getId());
        cliente.getContatos().remove(contato);

        clienteRepository.save(cliente);
        contatoRepository.delete(contato);

    }

    private void validarCelular(String celular) {
        if(contatoRepository.existsByCelular(celular)) {
            throw new ResourceAlreadyExistsException("Celular já cadastrado.");
        }
    }

}
