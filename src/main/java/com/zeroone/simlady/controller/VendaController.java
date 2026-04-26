package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.venda.AtualizarPagamentoVendaDto;
import com.zeroone.simlady.dto.venda.VendaComUsuarioDto;
import com.zeroone.simlady.dto.venda.VendaDetalheDto;
import com.zeroone.simlady.dto.venda.VendaRequestDto;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.mapper.VendaMapper;
import com.zeroone.simlady.service.VendaService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendas")
@Tag(name = "Vendas", description = "Gerenciamento de Vendas")
public class VendaController {

    private final VendaService vendaService;
    private final VendaMapper vendaMapper;

    @Operation(summary = "Cadastrar Venda", description = "Concluí um Pedido e fecha-lo como Venda")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda cadastrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDetalheDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<VendaDetalheDto> cadastrar(@Valid @RequestBody VendaRequestDto dto) {
        Venda venda = vendaMapper.toEntity(dto);
        vendaService.cadastrar(venda, dto.getPedidos());
        return ResponseEntity.status(201).body(vendaService.buscarDetalhe(venda.getId()));
    }

    @Operation(summary = "Listar todas as vendas", description = "Lista todas as vendas cadastradas no sistema com paginação, incluindo usuário resumido")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendas listadas na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = VendaComUsuarioDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem vendas na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<VendaComUsuarioDto>> listar(Pageable pageable) {
        Page<VendaComUsuarioDto> vendas = vendaService.listar(pageable);

        if(vendas.isEmpty()) {
            return ResponseEntity
                    .status(204)
                    .build();
        }
        return ResponseEntity
                .ok(vendas);
    }

    @Operation(summary = "Buscar venda por id", description = "Busca informações detalhadas da venda por id, incluindo pedido completo com itens")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDetalheDto.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<VendaDetalheDto> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(vendaService.buscarDetalhe(id));
    }

    @Operation(summary = "Atualizar status de pagamento", description = "Alterna o status de pagamento da venda")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDetalheDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content())
    })
    @PatchMapping("/{id}/pagamento")
    public ResponseEntity<VendaDetalheDto> atualizarPagamento(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPagamentoVendaDto dto) {
        return ResponseEntity.ok(vendaService.atualizarPagamento(id, dto.getPagamentoRealizado()));
    }

    @Operation(summary = "Deletar venda por id", description = "Deleta a venda pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venda deletada com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Venda não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        vendaService.deletar(id);
        return ResponseEntity
                .status(200)
                .build();
    }
}
