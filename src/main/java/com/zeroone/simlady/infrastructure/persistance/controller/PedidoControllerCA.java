package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.pedido.*;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaResponseDto;
import com.zeroone.simlady.core.application.usecases.pedido.*;
import com.zeroone.simlady.core.application.usecases.mensagem.EnviarPedidoCriadoUseCase;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.infrastructure.persistance.mapper.PedidoMapper;
import com.zeroone.simlady.infrastructure.persistance.mapper.VendaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "PedidosCA", description = "API para gerenciamento de pedidos")
public class PedidoControllerCA {

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;
    private final AtualizarPedidoUseCase atualizarPedidoUseCase;
    private final DeletarPedidoPorIdUseCase deletarPedidoPorIdUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ListarPedidosPorStatusUseCase listarPedidosPorStatusUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;
    private final FinalizarPedidoUseCase finalizarPedidoUseCase;
    private final EnviarPedidoCriadoUseCase enviarPedidoCriadoUseCase;
    private final VendaMapper vendaMapper;

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<PedidoResponseDto> criarPedido(@Valid @RequestBody PedidoCreateRequestDto request) {
        List<PedidoItem> itens = request.itens() != null 
            ? request.itens().stream()
                .map(PedidoMapper::toDomain)
                .toList()
            : new ArrayList<>();
        
        Pedido pedido = criarPedidoUseCase.executar(null, request.idUsuario(), itens);
        
        try {
            enviarPedidoCriadoUseCase.executar(pedido);
        } catch (Exception e) {
            System.err.println("Erro ao enviar pedido para RabbitMQ: " + e.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoMapper.toResponseDto(pedido));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os dados de um pedido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDto> buscarPorId(
            @Parameter(description = "ID único do pedido") @PathVariable UUID id) {
        Pedido pedido = buscarPedidoPorIdUseCase.executar(id);
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedido));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Estoque insuficiente para atualizar o pedido")
    })
    public ResponseEntity<PedidoResponseDto> atualizarPedido(
            @Parameter(description = "ID único do pedido") @PathVariable UUID id, 
            @Valid @RequestBody PedidoUpdateRequestDto request) {
        Pedido pedidoExistente = buscarPedidoPorIdUseCase.executar(id);
        
        // Processar os itens do request
        List<PedidoItem> novosItens = request.itens() != null 
            ? request.itens().stream()
                .map(PedidoMapper::toDomain)
                .toList()
            : new ArrayList<>();
        
        Pedido pedidoAtualizado = Pedido.of(
                pedidoExistente.getId(),
                pedidoExistente.getStatus(),
                request.idVenda(),
                request.idUsuario(),
                novosItens,
                pedidoExistente.getCriadoEm(),
                pedidoExistente.getAtualizadoEm()
        );
        
        Pedido pedidoSalvo = atualizarPedidoUseCase.executar(pedidoAtualizado);
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedidoSalvo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido", description = "Remove um pedido do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> deletarPedido(
            @Parameter(description = "ID único do pedido") @PathVariable UUID id) {
        deletarPedidoPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Retorna uma lista paginada de pedidos, opcionalmente filtrada por status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<Page<PedidoResponseDto>> listarPedidos(
            @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho,
            @Parameter(description = "Status do pedido para filtro") @RequestParam(required = false) String status) {
        
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
    @Operation(summary = "Alterar status do pedido", description = "Atualiza o status de um pedido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<PedidoResponseDto> alterarStatus(
            @Parameter(description = "ID único do pedido") @PathVariable UUID id,
            @Valid @RequestBody AlterarStatusPedidoRequestDto request) {
        Pedido pedido = alterarStatusPedidoUseCase.executar(id, request.status());
        return ResponseEntity.ok(PedidoMapper.toResponseDto(pedido));
    }

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar pedido", description = "Finaliza um pedido, atualiza estoque e cria venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido finalizado e venda criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente"),
            @ApiResponse(responseCode = "409", description = "Pedido não pode ser finalizado")
    })
    public ResponseEntity<VendaResponseDto> finalizarPedido(
            @Parameter(description = "ID único do pedido") @PathVariable UUID id,
            @Valid @RequestBody FinalizarPedidoRequestDto request) {
        try {
            Venda venda = finalizarPedidoUseCase.executar(id, request.desconto(), request.dataVenda());
            return ResponseEntity.ok(vendaMapper.toResponseDto(venda));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
