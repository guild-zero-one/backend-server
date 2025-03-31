package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.PedidoVendaDTO;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.service.PedidoVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoVendaController {

    @Autowired
    PedidoVendaService pedidoVendaService;

    @PostMapping("/{clienteId}")
    public ResponseEntity<PedidoVendaDTO> cadastrar (@RequestBody PedidoVenda pedidoVenda,
                                                     @PathVariable Integer clienteId) {
        return ResponseEntity
                .status(201)
                .body(pedidoVendaService
                        .cadastrar(pedidoVenda, clienteId));
    }

    @GetMapping
    public ResponseEntity<List<PedidoVendaDTO>> listar () {

        List<PedidoVendaDTO> pedidos = pedidoVendaService.listar();

        if(pedidos.isEmpty()) {
            return ResponseEntity.status(204).body(pedidos);
        }

        return ResponseEntity.status(200).body(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoVendaDTO> buscarPorId (@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoVendaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoVendaDTO>> buscarPorCliente (@PathVariable Integer id) {
        List<PedidoVendaDTO> pedidos = pedidoVendaService.buscarPorCliente(id);

        if(pedidos.isEmpty()) {
            return ResponseEntity.status(204).body(pedidos);
        }

        return ResponseEntity.status(200).body(pedidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoVendaDTO> atualizar (@PathVariable Integer id, @RequestBody PedidoVenda pedidoVenda) {
        return ResponseEntity.status(200)
                .body(pedidoVendaService
                        .atualizar(id, pedidoVenda));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoVendaDTO> atualizarStatus (
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity
                .status(200)
                .body(pedidoVendaService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover (@PathVariable Integer id) {
        pedidoVendaService.remover(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/status")
    public ResponseEntity<List<PedidoVendaDTO>> buscarTodosPorStatus(@RequestParam String status) {

        List<PedidoVendaDTO> pedidos = pedidoVendaService.buscarTodosPorStatus(status);

        if(pedidos.isEmpty()) {
            return ResponseEntity.status(204).body(pedidos);
        }

        return ResponseEntity.status(200).body(pedidos);

    }

}
