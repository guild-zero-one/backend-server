package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoRequestDto;
import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoResponseDto;
import com.zeroone.simlady.core.application.usecases.produto.CreateProdutoUseCase;
import com.zeroone.simlady.core.application.usecases.produto.ListProdutoUseCase;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.mapper.ProdutoMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.ProdutoRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/produtosCA")
public class ProdutoControllerCA {
    private final ProdutoRepositoryImpl repository;

    private final CreateProdutoUseCase createProdutoUseCase;

    private final ListProdutoUseCase listProdutoUseCase;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto>criarProduto(@RequestBody ProdutoRequestDto dto){
        Produto produto = createProdutoUseCase.executar(ProdutoMapper.toRawProduto(dto));
        return ResponseEntity.status(201).body(ProdutoMapper.toResponseDto(produto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>>listarProdutos(){
        List<Produto> produtoList = listProdutoUseCase.executar();
        return ResponseEntity.ok().body(produtoList.stream().map(ProdutoMapper::toResponseDto).toList());
    }
}
