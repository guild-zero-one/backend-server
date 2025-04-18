package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.item.PedidoItemResponseDto;
import com.zeroone.simlady.dto.pedido.PedidoVendaRequestDto;
import com.zeroone.simlady.dto.pedido.PedidoVendaResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.mapper.PedidoVendaMapper;
import com.zeroone.simlady.service.PedidoVendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoVendaController {

    private final PedidoVendaService pedidoVendaService;
    private final PedidoVendaMapper pedidoVendaMapper;

    @PostMapping
    public ResponseEntity<PedidoVendaResponseDto> cadastrar(@RequestBody @Valid PedidoVendaRequestDto dto) {
        PedidoVenda pedido = pedidoVendaMapper.toEntity(dto);
        pedidoVendaService.cadastrar(pedido);


        return ResponseEntity.status(201).body(pedidoVendaMapper.toDto(pedido));
    }

    @GetMapping
    public ResponseEntity<List<PedidoVendaResponseDto>> listarPedidos() {
        List<PedidoVenda> pedidos = pedidoVendaService.listar();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidoVendaMapper.toDto(pedidos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoVendaResponseDto> buscar(@PathVariable Integer id) {
        PedidoVenda pedido = pedidoVendaService.buscar(id);
        return ResponseEntity.ok(pedidoVendaMapper.toDto(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoVendaResponseDto> atualizar(@PathVariable Integer id, @Valid @RequestBody PedidoVendaRequestDto dto) {
        PedidoVenda atualizado = pedidoVendaService.atualizar(id, pedidoVendaMapper.toEntity(dto));
        return ResponseEntity.ok(pedidoVendaMapper.toDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        pedidoVendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoVendaResponseDto> atualizarStatus(
            @PathVariable Integer id,
            @RequestParam StatusPedido status
    ) {
        PedidoVenda atualizado = pedidoVendaService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedidoVendaMapper.toDto(atualizado));
    }
}
