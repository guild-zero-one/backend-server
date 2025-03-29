package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.cliente.ClienteRequestDto;
import com.zeroone.simlady.dto.cliente.ClienteResponseDto;
import com.zeroone.simlady.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<ClienteResponseDto> cadastrar(@RequestBody @Valid ClienteRequestDto dto) {

        return ResponseEntity.status(201)
                .body(clienteService
                        .cadastrar(dto));

    }

    @GetMapping
    public ResponseEntity <List<ClienteResponseDto>> listarClientes() {

        List<ClienteResponseDto> clientes = clienteService.listar();

        if(clientes.isEmpty()) {
            return ResponseEntity
                    .status(204)
                    .build();
        }

        return ResponseEntity
                .status(200)
                .body(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.ok(
                clienteService
                        .atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
