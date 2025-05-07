package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorRequestDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.service.FornecedorService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedores", description = "Marcas e Fornecedores")
public class FornecedorController {
    private final FornecedorService fornecedorService;

    private final FornecedorMapper fornecedorMapper;

    @Operation(summary = "Cadastrar fornecedor", description = "Cadastra um novo fornecedor/marca")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FornecedorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<FornecedorResponseDto> cadastrarFornecedor(@Valid @RequestBody FornecedorRequestDto dto) {
        Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
        FornecedorResponseDto response = fornecedorMapper.toResponseDto(fornecedorService.cadastrarFornecedor(fornecedor));
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Listar todos os fornecedores", description = "Lista todos os fornecedores cadastrados no sistema")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedores listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FornecedorResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem fornecedores na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<FornecedorResponseDto>> listar(Pageable pageable) {
        Page<FornecedorResponseDto> fornecedores = fornecedorService.listar(pageable)
                .map(fornecedorMapper::toResponseDto);

        if (fornecedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(fornecedores);
    }

    @Operation(summary = "Buscar fornecedor por id", description = "Busca fornecedor pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FornecedorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content()),
    })
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> buscarPorId(@PathVariable Integer id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);

        if(fornecedor == null){
            throw new ResourceNotFoundException("Fornecedor não encontrado");
        }

        return ResponseEntity.ok(fornecedorMapper.toResponseDto(fornecedor));
    }

    @Operation(summary = "Atualiza fornecedor", description = "Atualiza o fornecedor pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FornecedorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre fornecedores",
                    content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> atualizar(@PathVariable Integer id, @RequestBody FornecedorRequestDto dto){
        Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
        FornecedorResponseDto response = fornecedorMapper.toResponseDto(fornecedorService.atualizar(id,fornecedor));
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "Deletar fornecedor por id", description = "Deleta fornecedor pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id) {
        fornecedorService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Listar fornecedores com produtos (paginado)",
            description = "Lista os fornecedores junto com os seus respectivos produtos de forma paginada"
    )
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedores e produtos listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FornecedorComProdutosResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum fornecedor encontrado com produtos",
                    content = @Content()
            )
    })
    @GetMapping("/com-produtos")
    public ResponseEntity<Page<FornecedorComProdutosResponseDto>> listarFornecedoresComProdutos(Pageable pageable) {
        Page<FornecedorComProdutosResponseDto> resposta = fornecedorService.listarFornecedoresComProdutos(pageable);

        if (resposta.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resposta);
    }
}
