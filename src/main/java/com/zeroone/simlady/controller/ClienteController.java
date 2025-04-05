package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.cliente.ClienteRequestDto;
import com.zeroone.simlady.dto.cliente.ClienteResponseDto;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.mapper.ClienteMapper;
import com.zeroone.simlady.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @PostMapping()
    public ResponseEntity<ClienteResponseDto> cadastrar(@RequestBody @Valid ClienteRequestDto dto) {

        Cliente cliente = clienteMapper.toEntity(dto);
        return ResponseEntity.status(201)
                .body(clienteMapper
                        .toDto(clienteService
                                .cadastrar(cliente)));
    }

    @GetMapping
    public ResponseEntity <List<ClienteResponseDto>> listarClientes() {

        List<Cliente> clientes = clienteService.listar();

        if(clientes.isEmpty()) {
            return ResponseEntity
                    .status(204)
                    .build();
        }

        return ResponseEntity
                .status(200)
                .body(clientes
                        .stream()
                        .map(clienteMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscar(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscar(id);
        return ResponseEntity.ok(clienteMapper.toDto(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteRequestDto dto) {
       Cliente cliente = clienteMapper.toEntity(dto);

       clienteService
                .atualizar(id, cliente);

       return ResponseEntity.ok(clienteMapper.toDto(cliente));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
         clienteService.desativarCliente(id);
         return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
