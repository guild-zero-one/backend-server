package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.ProdutoDTO;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar() {
        List<ProdutoDTO> produtos = produtoService.listar();
        if (produtos.isEmpty()){
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(ProdutoMapper.toDTO(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        produtoDTO.setId(null);
        return ResponseEntity.ok(produtoService.cadastrarProduto(produtoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Integer id) {
        produtoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO){
        return ResponseEntity.ok(produtoService.atualizar(id,produtoDTO));
    }
}
