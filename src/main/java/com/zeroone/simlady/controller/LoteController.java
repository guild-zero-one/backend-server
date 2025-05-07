package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.lote.LoteRequestDto;
import com.zeroone.simlady.dto.lote.LoteResponseDto;
import com.zeroone.simlady.dto.lote.LoteResponseItemDto;
import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.LoteItemMapper;
import com.zeroone.simlady.mapper.LoteMapper;
import com.zeroone.simlady.service.LoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@Tag(name = "Lotes", description = "Estoque de Produto")
public class LoteController {
    private final LoteService loteService;

    private final LoteMapper loteMapper;

    private final LoteItemMapper loteItemMapper;

    @Operation(summary = "Cadastrar lote", description = "Cadastra um novo lote e seus itens")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lote cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<LoteResponseItemDto> cadastrarLote(@RequestBody LoteRequestDto loteDto) {
        Lote lote = loteMapper.toEntity(loteDto);
        List<LoteItemRequestDto> loteItems = loteDto.getLoteItems();

        Lote loteCadastrado = loteService.cadastrarLote(lote);

        List<LoteItemRequestDto> loteItemsComId = loteItems.stream().peek(loteItemRequestDto -> loteItemRequestDto.setLoteId(loteCadastrado.getId())).toList();
        List<LoteItem> loteItemsParaSalvar = loteItemsComId.stream()
                .map(loteItemMapper::toEntity)
                .toList();

        List<LoteItem> loteItemCadastrados = loteService.cadastrarLoteItem(loteItemsParaSalvar);

        List<LoteItemResponseDto> loteItemResponse = loteItemCadastrados.stream()
                .map(loteItemMapper::toResponseDto)
                .toList();
        return ResponseEntity.status(201).body(loteMapper.toResponseItemDto(loteCadastrado, loteItemResponse));
    }

    @Operation(summary = "Listar lotes", description = "Lista todos os lotes cadastrados no sistema")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lotes listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoteResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem lotes na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<LoteResponseDto>> listar(Pageable pageable) {
        Page<LoteResponseDto> lotes = loteService.listar(pageable)
                .map(loteMapper::toResponseDto);

        if (lotes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lotes);
    }

    @Operation(summary = "Buscar lote por id", description = "Busca lote pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lote encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDto> buscarPorId(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id);
        return ResponseEntity.ok(loteMapper.toResponseDto(lote));
    }

    @Operation(summary = "Atualizar lote por id", description = "Atualiza lote pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lote atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre lotes",
                    content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<LoteResponseDto> atualizarLote(@PathVariable Integer id, @RequestBody LoteRequestDto loteDto) {
        Lote lote = loteMapper.toEntity(loteDto);
        Lote loteAtualizado = loteService.atualizarLote(id, lote);
        return ResponseEntity.ok(loteMapper.toResponseDto(loteAtualizado));
    }

    @Operation(summary = "Atualizar itens do lote por id", description = "Atualiza itens do lote pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens do lote atualizados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Itens do lote não encontrados",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre itens do lote",
                    content = @Content()),
    })
    @PatchMapping("/lote-items/{id}")
    public ResponseEntity<List<LoteItemResponseDto>> atualizarLoteItem(@PathVariable Integer id, @RequestBody List<LoteItemRequestDto> loteItems) {
        List<LoteItem> loteItemsAtualizados = loteItems.stream()
                .map(loteItemMapper::toEntity)
                .toList();

        List<LoteItem> loteItemAtualizados = loteService.atualizarLoteItem(id, loteItemsAtualizados);

        return ResponseEntity.ok(loteItemAtualizados.stream()
                .map(loteItemMapper::toResponseDto)
                .toList());
    }

    @Operation(summary = "Deletar lote por id", description = "Delete lote pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lote deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLote(@PathVariable Integer id) {
        loteService.deletarLote(id);
        return ResponseEntity.noContent().build();
    }
}
