package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.produto.*;
import com.zeroone.simlady.core.application.usecases.produto.*;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.mapper.ProdutoMapper;
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

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@Tag(name = "ProdutosCA", description = "API para gerenciamento de produtos")
@RequiredArgsConstructor
public class ProdutoControllerCA {
    
    private final CriarProdutoUseCase criarProdutoUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final ListarProdutoUseCase listarProdutoUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase;
    private final BuscarProdutoPorSkuUseCase buscarProdutoPorSkuUseCase;
    private final ListarProdutosPorMarcaUseCase listarProdutosPorMarcaUseCase;
    private final ProdutoMapper produtoMapper;
    
    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDto> criarProduto(@Valid @RequestBody ProdutoRequestDto request) {
        Produto produto = criarProdutoUseCase.executar(produtoMapper.toDomainFromRequest(request));
        ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os dados de um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorId(
            @Parameter(description = "ID único do produto") @PathVariable UUID id) {
        try {
            Produto produto = buscarProdutoPorIdUseCase.executar(id);
            ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista paginada de produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<Page<ProdutoResponseDto>> listarProdutos(
            @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Produto> produtos = listarProdutoUseCase.executar(pagina, tamanho);
        Page<ProdutoResponseDto> response = produtos.map(produtoMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDto> atualizarProduto(
            @Parameter(description = "ID único do produto") @PathVariable UUID id,
            @Valid @RequestBody ProdutoUpdateRequestDto request) {
        
        Optional<Produto> produto = atualizarProdutoUseCase.executar(
                id,
                request.getNome(),
                request.getSku(),
                request.getDescricao(),
                request.getTag(),
                request.getQuantidade(),
                request.getPrecoUnitario(),
                request.getCatalogo(),
                request.getValorVenda(),
                request.getImagemUrl(),
                request.getIdMarca()
        );
        
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProdutoResponseDto response = produtoMapper.toResponseDto(produto.get());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deletarProduto(
            @Parameter(description = "ID único do produto") @PathVariable UUID id) {
        deletarProdutoPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar produto por SKU", description = "Retorna os dados de um produto específico pelo seu SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorSku(
            @Parameter(description = "SKU do produto") @RequestParam String sku) {
        try {
            Produto produto = buscarProdutoPorSkuUseCase.executar(sku);
            ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/marca/{idMarca}")
    @Operation(summary = "Listar produtos por marca", description = "Retorna uma lista paginada de produtos de uma marca específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<Page<ProdutoResponseDto>> listarProdutosPorMarca(
            @Parameter(description = "ID único da marca") @PathVariable UUID idMarca,
            @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Produto> produtos = listarProdutosPorMarcaUseCase.executar(idMarca, pagina, tamanho);
        Page<ProdutoResponseDto> response = produtos.map(produtoMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
}
