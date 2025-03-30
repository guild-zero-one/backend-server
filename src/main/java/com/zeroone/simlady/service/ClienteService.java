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

    private final ClienteMapper clienteMapper;


    public ClienteResponseDto cadastrar(ClienteRequestDto dto) {

        validarCpf(dto.getCpf());
        validarEmail(dto.getEmail());

        Cliente cliente = clienteMapper.toEntity(dto);
        clienteRepository.save(cliente);

        return clienteMapper.toDto(cliente);
    }

    public List<ClienteResponseDto> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }

    public ClienteResponseDto buscar(Integer id) {
        return clienteMapper.toDto(buscarEntidade(id));
    }

    public ClienteResponseDto atualizar(Integer id, ClienteRequestDto dto) {

        buscarEntidade(id);

        boolean existePorCpf = clienteRepository.existsByCpfAndIdNot(dto.getCpf(), id);
        boolean existePorEmail = clienteRepository.existsByEmailAndIdNot(dto.getEmail(), id);

        if(existePorCpf && existePorEmail) {
            throw new ResourceAlreadyExistsException("Dados inválidos, email e/ou cpf já cadastrados");
        }

        Cliente cliente = clienteMapper.toEntity(dto);

        cliente.setId(id);
        clienteRepository.save(cliente);

        return clienteMapper.toDto(cliente);
    }

    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);
    }

    public Cliente buscarEntidade(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
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

