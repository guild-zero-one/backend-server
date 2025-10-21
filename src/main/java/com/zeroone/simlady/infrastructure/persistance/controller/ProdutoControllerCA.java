package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.produto.*;
import com.zeroone.simlady.core.application.usecases.produto.*;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.mapper.ProdutoMapper;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/produtosCA")
@Tag(name = "ProdutosCA", description = "API para gerenciamento de produtos")
@RequiredArgsConstructor
public class ProdutoControllerCA {
    
    private final CriarProdutoUseCase criarProdutoUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final ListarProdutoUseCase listarProdutoUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase;
    private final BuscarProdutoPorSkuUseCase buscarProdutoPorSkuUseCase;
    private final ListarProdutosPorFornecedorUseCase listarProdutosPorFornecedorUseCase;
    private final ProdutoMapper produtoMapper;
    
    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ProdutoResponseDto> criarProduto(@Valid @RequestBody ProdutoRequestDto request) {
        Produto produto = criarProdutoUseCase.executar(produtoMapper.toDomainFromRequest(request));
        ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorId(@PathVariable UUID id) {
        try {
            Produto produto = buscarProdutoPorIdUseCase.executar(id);
            ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public ResponseEntity<Page<ProdutoResponseDto>> listarProdutos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Produto> produtos = listarProdutoUseCase.executar(pagina, tamanho);
        Page<ProdutoResponseDto> response = produtos.map(produtoMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProdutoResponseDto> atualizarProduto(
            @PathVariable UUID id,
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
                request.getImagemUrl()
        );
        
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProdutoResponseDto response = produtoMapper.toResponseDto(produto.get());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID id) {
        deletarProdutoPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar produto por SKU")
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorSku(@RequestParam String sku) {
        try {
            Produto produto = buscarProdutoPorSkuUseCase.executar(sku);
            ProdutoResponseDto response = produtoMapper.toResponseDto(produto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "Listar produtos por fornecedor")
    public ResponseEntity<Page<ProdutoResponseDto>> listarProdutosPorFornecedor(
            @PathVariable UUID idFornecedor,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Produto> produtos = listarProdutosPorFornecedorUseCase.executar(idFornecedor, pagina, tamanho);
        Page<ProdutoResponseDto> response = produtos.map(produtoMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
}
