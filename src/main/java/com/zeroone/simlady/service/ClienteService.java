package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.cliente.ClienteRequestDto;
import com.zeroone.simlady.dto.cliente.ClienteResponseDto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ClienteMapper;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public Cliente cadastrar(Cliente cliente) {

        validarCpf(cliente.getCpf());
        validarEmail(cliente.getEmail());

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscar(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Cliente atualizar(Integer id, Cliente cliente) {

        buscar(id);

        boolean existePorCpf = clienteRepository.existsByCpfAndIdNot(cliente.getCpf(), id);
        boolean existePorEmail = clienteRepository.existsByEmailAndIdNot(cliente.getEmail(), id);

        if(existePorCpf && existePorEmail) {
            throw new ResourceAlreadyExistsException("Dados inválidos, email e/ou cpf já cadastrados");
        }

        cliente.setId(id);

        return clienteRepository.save(cliente);
    }

    public void desativarCliente(Integer id) {
        Cliente cliente = buscar(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);
    }

    private void validarCpf(String cpf) {
        if(clienteRepository.existsByCpf(cpf)) {
            throw new ResourceAlreadyExistsException("CPF já cadastrado!");
        }
    }

    private void validarEmail(String email) {
        if(clienteRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email já cadastrado!");
        }
    }
}

