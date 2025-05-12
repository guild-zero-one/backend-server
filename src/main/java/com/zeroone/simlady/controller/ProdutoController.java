package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutoRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.service.FornecedorService;
import com.zeroone.simlady.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de Produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;
    private final FornecedorService fornecedorService;

    @Operation(summary = "Cadastrar produto", description = "Cadastra um novo produto no sistema")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrarProduto(@RequestBody ProdutoRequestDto dto) {
        Produto produto = produtoMapper.toEntity(dto);

        Fornecedor fornecedor = fornecedorService.buscarPorId(dto.getFornecedorId());
        produto.setFornecedor(fornecedor);

        ProdutoResponseDto response = produtoMapper.toResponseDto(produtoService.cadastrarProduto(produto));
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Listar produtos", description = "Lista todos os produtos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "array", implementation = ProdutoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem produtos na base",
                    content = @Content()),
    })
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> listar(Pageable pageable) {
        Page<ProdutoResponseDto> produtos = produtoService.listar(pageable)
                .map(produtoMapper::toResponseDto);

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Buscar produto por id", description = "Busca um produto por id, caso exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoMapper.toResponseDto(produto));
    }

    @Operation(summary = "Buscar produtos por id do fornecedor", description = "Buscar produtos por id do fornecedor, caso exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado",
                    content = @Content())
    })
    @GetMapping("/fornecedor/{id}")
    public ResponseEntity<List<ProdutoResponseDto>> buscarPorFornecedor(@PathVariable Integer id) {
        List<Produto> produtos = produtoService.buscarPorFornecedor(id);

        if (produtos.isEmpty()) {
            ResponseEntity.notFound().build();
        }

        List<ProdutoResponseDto> produtosResponseDto = produtos.stream().map(produtoMapper::toResponseDto).toList();

        return ResponseEntity.ok(produtosResponseDto);
    }

    @Operation(summary = "Atualizar produto por id", description = "Atualiza um produto por id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre produtos",
                    content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoRequestDto request){
        Produto produto = produtoMapper.toEntity(request);
        ProdutoResponseDto response = produtoMapper.toResponseDto(produtoService.atualizar(id,produto));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar produto por id", description = "Deleta um produto por id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Integer id) {
        produtoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}
