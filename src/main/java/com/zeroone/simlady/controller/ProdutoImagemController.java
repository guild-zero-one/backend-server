package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemPatchDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemRequestDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.entity.ProdutoImagem;
import com.zeroone.simlady.mapper.ProdutoImagemMapper;
import com.zeroone.simlady.service.ProdutoImagemService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos/imagens")
@Tag(name = "Imagens", description = "Imagens de cada Produto")
public class ProdutoImagemController {
    private final ProdutoImagemService produtoImagemService;

    private final ProdutoImagemMapper produtoImagemMapper;

    @Operation(summary = "Cadastrar imagens", description = "Cadastra uma nova imagem de um determinado produto")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem cadastrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoImagemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<ProdutoImagemResponseDto> cadastrarImagem(@RequestBody ProdutoImagemRequestDto produtoImagemDto) {
        ProdutoImagem imagem = produtoImagemMapper.toEntity(produtoImagemDto);

        ProdutoImagem imagemCadastrada = produtoImagemService.cadastrarImagem(imagem);

        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagemCadastrada);
        return ResponseEntity.status(201).body(imagemResponse);
    }


    @Operation(summary = "Listar todas as imagens", description = "Lista todas as imagens de produtos do sistema de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagens listadas na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoImagemResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem imagens na base",
                    content = @Content()),
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<Page<ProdutoImagemResponseDto>> listarImagens(Pageable pageable) {
        Page<ProdutoImagemResponseDto> imagens = produtoImagemService.listarImagens(pageable)
                .map(produtoImagemMapper::toResponseDto);

        if (imagens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(imagens);
    }

    @Operation(summary = "Buscar imagem por id", description = "Busca uma imagem por id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoImagemResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponseDto> buscarImagemPorId(@PathVariable Integer id) {
        ProdutoImagem imagem = produtoImagemService.buscarImagemPorId(id);
        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagem);
        return ResponseEntity.ok(imagemResponse);
    }

    @Operation(summary = "Atualizar imagem", description = "Atualiza a imagem pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoImagemResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre imagens",
                    content = @Content()),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponseDto> atualizarImagem(@PathVariable Integer id, @RequestBody ProdutoImagemPatchDto produtoImagemDto) {
        ProdutoImagem imagemAtualizada = produtoImagemMapper.toEntity(produtoImagemDto);

        ProdutoImagem imagem = produtoImagemService.atualizarImagem(id, imagemAtualizada);

        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagem);
        return ResponseEntity.ok(imagemResponse);
    }

    @Operation(summary = "Deletar imagem", description = "Deleta a imagem com o id informado, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem deletada com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImagem(@PathVariable Integer id) {
        produtoImagemService.deletarImagem(id);
        return ResponseEntity.noContent().build();
    }
}
