package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.pedido.PedidoVendaRequestDto;
import com.zeroone.simlady.dto.pedido.PedidoVendaResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.mapper.PedidoVendaMapper;
import com.zeroone.simlady.service.PedidoVendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Pedido de Vendas de cada Cliente")
public class PedidoVendaController {

    private final PedidoVendaService pedidoVendaService;
    private final PedidoVendaMapper pedidoVendaMapper;

    @Operation(summary = "Cadastrar um Pedido de Venda", description = "Cadastra um novo Pedido de Venda e seus itens")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoVendaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<PedidoVendaResponseDto> cadastrar(@RequestBody @Valid PedidoVendaRequestDto dto) {

        PedidoVenda pedido = pedidoVendaMapper.toEntity(dto);
        pedidoVendaService.cadastrar(pedido);

        return ResponseEntity.status(201).body(pedidoVendaMapper.toDto(pedido));
    }

    @Operation(summary = "Listar todos os pedidos", description = "Lista todos os Pedidos de Venda cadastrados no sistema")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoVendaResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem pedidos na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<List<PedidoVendaResponseDto>> listarPedidos() {
        List<PedidoVenda> pedidos = pedidoVendaService.listar();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidoVendaMapper.toDto(pedidos));
    }

    @Operation(summary = "Buscar pedido por id", description = "Busca pedido por id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoVendaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoVendaResponseDto> buscar(@PathVariable Integer id) {
        PedidoVenda pedido = pedidoVendaService.buscar(id);
        return ResponseEntity.ok(pedidoVendaMapper.toDto(pedido));
    }

    @Operation(summary = "Atualizar pedido por id", description = "Atualiza pedido pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoVendaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre pedidos",
                    content = @Content()),
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoVendaResponseDto> atualizar(@PathVariable Integer id, @Valid @RequestBody PedidoVendaRequestDto dto) {
        PedidoVenda atualizado = pedidoVendaService.atualizar(id, pedidoVendaMapper.toEntity(dto));
        return ResponseEntity.ok(pedidoVendaMapper.toDto(atualizado));
    }

    @Operation(summary = "Atualizar o status do pedido", description = "Atualiza status do pedido pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoVendaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content()),
    })
    @PatchMapping("/status/{id}")
    public ResponseEntity<PedidoVendaResponseDto> atualizarStatus(
            @PathVariable Integer id,
            @RequestParam StatusPedido status
    ) {
        PedidoVenda atualizado = pedidoVendaService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedidoVendaMapper.toDto(atualizado));
    }

    @Operation(summary = "Deletar pedido por id", description = "Deleta o pedido pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        pedidoVendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
