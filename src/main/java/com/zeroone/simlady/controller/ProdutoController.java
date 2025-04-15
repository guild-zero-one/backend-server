package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutoRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.service.FornecedorService;
import com.zeroone.simlady.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    private final ProdutoMapper produtoMapper;

    private final FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar() {
        List<ProdutoResponseDto> produtos = produtoService.listar().stream().map(produtoMapper::toResponseDto).toList();
        if (produtos.isEmpty()){
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoMapper.toResponseDto(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrarProduto(@RequestBody ProdutoRequestDto dto) {
        Produto produto = produtoMapper.toEntity(dto);
        Fornecedor fornecedor = fornecedorService.buscarPorId(dto.getFornecedorId());
        produto.setFornecedor(fornecedor);
        ProdutoResponseDto response = produtoMapper.toResponseDto(produtoService.cadastrarProduto(produto));
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Integer id) {
        produtoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoRequestDto request){
        Produto produto = produtoMapper.toEntity(request);
        ProdutoResponseDto response = produtoMapper.toResponseDto(produtoService.atualizar(id,produto));
        return ResponseEntity.ok(response);
    }
}
