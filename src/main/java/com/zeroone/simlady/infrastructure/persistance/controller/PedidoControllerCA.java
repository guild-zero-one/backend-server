package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.pedido.*;
import com.zeroone.simlady.core.application.usecases.pedido.*;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.infrastructure.persistance.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidosCA")
@RequiredArgsConstructor
public class PedidoControllerCA {

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;
    private final AtualizarPedidoUseCase atualizarPedidoUseCase;
    private final DeletarPedidoPorIdUseCase deletarPedidoPorIdUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ListarPedidosPorStatusUseCase listarPedidosPorStatusUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> criarPedido(@RequestBody PedidoCreateRequestDto request) {
        List<PedidoItem> itens = request.itens() != null 
            ? request.itens().stream()
                .map(PedidoMapper::toDomain)
                .toList()
            : new ArrayList<>();
        
        Pedido pedido = criarPedidoUseCase.executar(null, request.idUsuario(), itens);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoMapper.toResponseDto(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> buscarPorId(@PathVariable UUID id) {
        Pedido pedido = buscarPedidoPorIdUseCase.executar(id);
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> atualizarPedido(@PathVariable UUID id, @RequestBody PedidoUpdateRequestDto request) {
        Pedido pedidoExistente = buscarPedidoPorIdUseCase.executar(id);
        
        Pedido pedidoAtualizado = Pedido.of(
                pedidoExistente.getId(),
                pedidoExistente.getStatus(),
                request.idVenda(),
                request.idUsuario(),
                pedidoExistente.getItens(),
                pedidoExistente.getCriadoEm(),
                pedidoExistente.getAtualizadoEm()
        );
        
        Pedido pedidoSalvo = atualizarPedidoUseCase.executar(pedidoAtualizado);
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedidoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable UUID id) {
        deletarPedidoPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDto>> listarPedidos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(required = false) String status) {
        
        Page<Pedido> pedidos;
        if (status != null && !status.isEmpty()) {
            pedidos = listarPedidosPorStatusUseCase.executar(status, pagina, tamanho);
        } else {
            pedidos = listarPedidosUseCase.executar(pagina, tamanho);
        }
        
        Page<PedidoResponseDto> response = pedidos.map(PedidoMapper::toResponseDto);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDto> alterarStatus(
            @PathVariable UUID id,
            @RequestBody AlterarStatusPedidoRequestDto request) {
        Pedido pedido = alterarStatusPedidoUseCase.executar(id, request.status());
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedido));
    }

}
