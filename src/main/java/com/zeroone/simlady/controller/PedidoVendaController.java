package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.pedido.PedidoVendaRequestDto;
import com.zeroone.simlady.dto.pedido.PedidoDetalheResponseDto;
import com.zeroone.simlady.dto.pedido.PedidoResumoResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.mapper.PedidoVendaMapper;
import com.zeroone.simlady.mapper.PedidoVendaResponseMapper;
import com.zeroone.simlady.service.PedidoVendaService;
import com.zeroone.simlady.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Pedido de Vendas de cada Cliente")
public class PedidoVendaController {

    private final PedidoVendaService pedidoVendaService;
    private final PedidoVendaMapper pedidoVendaMapper;
    private final PedidoVendaResponseMapper pedidoVendaResponseMapper;
    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastrar um Pedido de Venda", description = "Cadastra um novo Pedido de Venda e seus itens")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDetalheResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<PedidoDetalheResponseDto> cadastrar(@RequestBody @Valid PedidoVendaRequestDto dto) {

        PedidoVenda pedido = pedidoVendaMapper.toEntity(dto);
        PedidoVenda pedidoCadastrado = pedidoVendaService.cadastrar(pedido);

        return ResponseEntity.status(201).body(pedidoVendaResponseMapper.toDetalhe(pedidoCadastrado));
    }

    @Operation(summary = "Listar todos os pedidos", description = "Lista todos os Pedidos de Venda cadastrados no sistema com paginação e suporte a busca. " +
            "Usuários com permissão 'comum' recebem apenas os próprios pedidos, independentemente do valor informado em 'idUsuario'.")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResumoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem pedidos na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<PedidoResumoResponseDto>> listarPedidos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) UUID idUsuario,
            HttpServletRequest request,
            Pageable pageable) {
        UUID idUsuarioFiltro = resolverIdUsuarioFiltro(idUsuario, request);

        Page<PedidoResumoResponseDto> pedidos = pedidoVendaService
                .listarComFiltros(search, status, idUsuarioFiltro, pageable)
                .map(pedidoVendaResponseMapper::toResumo);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    /**
     * Usuários 'comum' só podem ver os próprios pedidos: o id da query string é ignorado
     * e substituído pelo id extraído do usuário autenticado, prevenindo IDOR. Para 'ADMIN'
     * o valor informado (podendo ser nulo) é respeitado.
     */
    private UUID resolverIdUsuarioFiltro(UUID idUsuario, HttpServletRequest request) {
        Usuario usuarioAutenticado = usuarioService.buscarAutenticado(request);

        if (usuarioAutenticado.getPermissao() == Permissao.COMUM) {
            return usuarioAutenticado.getId();
        }

        return idUsuario;
    }

    @Operation(summary = "Buscar pedido por id", description = "Busca pedido por id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDetalheResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalheResponseDto> buscar(@PathVariable UUID id) {
        PedidoVenda pedido = pedidoVendaService.buscarDetalhe(id);
        return ResponseEntity.ok(pedidoVendaResponseMapper.toDetalhe(pedido));
    }

    @Operation(summary = "Listar pedidos por usuário", description = "Lista pedidos de um usuário com paginação e filtro por status")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResumoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem pedidos para o usuário",
                    content = @Content()),
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<PedidoResumoResponseDto>> listarPedidosPorUsuario(
            @PathVariable UUID usuarioId,
            @RequestParam(required = false) StatusPedido status,
            Pageable pageable
    ) {
        Page<PedidoResumoResponseDto> pedidos = pedidoVendaService
                .listarPorUsuarioComFiltros(usuarioId, status, pageable)
                .map(pedidoVendaResponseMapper::toResumo);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Atualizar pedido por id", description = "Atualiza pedido pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDetalheResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre pedidos",
                    content = @Content()),
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDetalheResponseDto> atualizar(@PathVariable UUID id, @Valid @RequestBody PedidoVendaRequestDto dto) {
        PedidoVenda atualizado = pedidoVendaService.atualizar(id, pedidoVendaMapper.toEntity(dto));
        return ResponseEntity.ok(pedidoVendaResponseMapper.toDetalhe(atualizado));
    }

    @Operation(summary = "Atualizar o status do pedido", description = "Atualiza status do pedido pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDetalheResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content()),
    })
    @PatchMapping("/status/{id}")
    public ResponseEntity<PedidoDetalheResponseDto> atualizarStatus(
            @PathVariable UUID id,
            @RequestParam StatusPedido status
    ) {
        PedidoVenda atualizado = pedidoVendaService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedidoVendaResponseMapper.toDetalhe(atualizado));
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
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        pedidoVendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
