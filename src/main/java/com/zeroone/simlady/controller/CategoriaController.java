package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.categoria.CategoriaRequestDto;
import com.zeroone.simlady.dto.categoria.CategoriaResponseDto;
import com.zeroone.simlady.entity.Categoria;
import com.zeroone.simlady.mapper.CategoriaMapper;
import com.zeroone.simlady.service.CategoriaService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Gerenciamento de Categorias de Produtos")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria de produtos")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Categoria com este nome já existe",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> criar(@Valid @RequestBody CategoriaRequestDto dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria criada = categoriaService.criar(categoria);
        return ResponseEntity.status(201).body(categoriaMapper.toResponseDto(criada));
    }

    @Operation(summary = "Listar categorias", description = "Lista categorias cadastradas no sistema com suporte a busca por nome e paginação")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem categorias na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDto>> listar(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<CategoriaResponseDto> categorias;

        if (search != null && !search.trim().isEmpty()) {
            categorias = categoriaService.buscarPorNome(search, pageable)
                    .map(categoriaMapper::toResponseDto);
        } else {
            categorias = categoriaService.listar(pageable)
                    .map(categoriaMapper::toResponseDto);
        }

        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categoria por ID", description = "Busca uma categoria específica pelo ID")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content()),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> buscarPorId(@PathVariable UUID id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoriaMapper.toResponseDto(categoria));
    }

    @Operation(summary = "Listar categorias de um produto", description = "Lista todas as categorias vinculadas a um produto específico")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Nenhuma categoria encontrada",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content()),
    })
    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<List<CategoriaResponseDto>> buscarCategoriasPorProduto(@PathVariable UUID produtoId) {
        List<CategoriaResponseDto> categorias = categoriaService.buscarCategoriasPorProduto(produtoId)
                .stream()
                .map(categoriaMapper::toResponseDto)
                .toList();

        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente pelo ID")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Categoria com este nome já existe",
                    content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CategoriaRequestDto dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria atualizada = categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok(categoriaMapper.toResponseDto(atualizada));
    }

    @Operation(summary = "Deletar categoria", description = "Deleta uma categoria pelo ID")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

