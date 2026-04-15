package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutoRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoLoteRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
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

    @Operation(summary = "Cadastrar produtos em lote", description = "Cadastra múltiplos produtos em uma única requisição. Cria fornecedores automaticamente se não existirem.")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produtos processados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoLoteResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
    })
    @PostMapping("/lote")
    public ResponseEntity<ProdutoLoteResponseDto> cadastrarEmLote(
            @Valid @RequestBody List<ProdutoLoteRequestDto> produtosDto) {
        ProdutoLoteResponseDto response = produtoService.cadastrarEmLote(produtosDto);
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
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable UUID id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoMapper.toResponseDto(produto));
    }

    @Operation(summary = "Buscar produtos por lista de ids", description = "Busca produtos por uma lista de ids, caso existam")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "array", implementation = ProdutoResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado",
                    content = @Content())
    })
    @GetMapping("/lista")
    public ResponseEntity<List<ProdutoResponseDto>> buscarListaPorId(@RequestParam List<UUID> ids) {
        List<ProdutoResponseDto> produtos = produtoService.buscarListaPorId(ids)
                .stream()
                .map(produtoMapper::toResponseDto)
                .toList();

        if (produtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Buscar produto por sku", description = "Busca produto pelo sku, caso existam")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "array", implementation = ProdutoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado",
                    content = @Content())
    })
    @GetMapping("/sku")
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorSku(@RequestParam String sku) {
        Produto produtoExistente = produtoService.buscarProdutoPorSku(sku);

        if (produtoExistente != null) {
            return ResponseEntity.ok(produtoMapper.toResponseDto(produtoExistente));
        } else {
            return ResponseEntity.noContent().build();
        }
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
    public ResponseEntity<List<ProdutoResponseDto>> buscarPorFornecedor(@PathVariable UUID id) {
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
    public ResponseEntity<ProdutoResponseDto> atualizarProduto(@PathVariable UUID id, @RequestBody ProdutoRequestDto request){
        Produto produto = produtoMapper.toEntity(request);
        ProdutoResponseDto response = produtoMapper.toResponseDto(produtoService.atualizar(id,produto));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar produtos por fornecedor com filtro", description = "Lista produtos de um fornecedor específico com busca por nome e paginação")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado",
                    content = @Content()),
    })
    @GetMapping("/fornecedor/{fornecedorId}/search")
    public ResponseEntity<Page<ProdutoResponseDto>> buscarPorFornecedorComFiltro(
            @PathVariable UUID fornecedorId,
            @RequestParam(required = false, defaultValue = "") String nome,
            Pageable pageable) {
        Page<ProdutoResponseDto> produtos = produtoService.listarPorFornecedorComFiltro(fornecedorId, nome, pageable)
                .map(produtoMapper::toResponseDto);

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Buscar produtos por categoria", description = "Lista produtos de uma categoria específica com busca por nome e paginação")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado",
                    content = @Content()),
    })
    @GetMapping("/categoria/search")
    public ResponseEntity<Page<ProdutoResponseDto>> buscarPorCategoria(
            @RequestParam String nome,
            Pageable pageable) {
        Page<ProdutoResponseDto> produtos = produtoService.listarPorCategoria(nome, pageable)
                .map(produtoMapper::toResponseDto);

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Contar pedidos distintos por produto", description = "Retorna a quantidade de pedidos distintos em que o produto aparece")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class, example = "5"))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content()),
    })
    @GetMapping("/{id}/pedidos/count")
    public ResponseEntity<Long> contarPedidosDistintos(@PathVariable UUID id) {
        Long count = produtoService.contarPedidosDistintosPorProduto(id);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Associar categoria ao produto", description = "Associa uma categoria a um produto")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria associada com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Categoria já associada ao produto",
                    content = @Content()),
    })
    @PostMapping("/{produtoId}/categorias/{categoriaId}")
    public ResponseEntity<Void> associarCategoria(
            @PathVariable UUID produtoId,
            @PathVariable UUID categoriaId) {
        produtoService.associarCategoria(produtoId, categoriaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desassociar categoria do produto", description = "Remove a associação de uma categoria a um produto")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria desassociada com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Produto, categoria ou associação não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{produtoId}/categorias/{categoriaId}")
    public ResponseEntity<Void> desassociarCategoria(
            @PathVariable UUID produtoId,
            @PathVariable UUID categoriaId) {
        produtoService.desassociarCategoria(produtoId, categoriaId);
        return ResponseEntity.noContent().build();
    }
}
