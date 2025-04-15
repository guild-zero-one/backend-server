package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemPatchDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemRequestDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemResponseDto;
import com.zeroone.simlady.entity.ProdutoImagem;
import com.zeroone.simlady.mapper.ProdutoImagemMapper;
import com.zeroone.simlady.service.ProdutoImagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/produtos/imagens")
public class ProdutoImagemController {
    private final ProdutoImagemService produtoImagemService;

    private final ProdutoImagemMapper produtoImagemMapper;

    @GetMapping
    public ResponseEntity<List<ProdutoImagemResponseDto>> listarImagens() {
        List<ProdutoImagemResponseDto> imagens = produtoImagemService.listarImagens().stream()
                .map(produtoImagemMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imagens);
    }

    @PostMapping
    public ResponseEntity<ProdutoImagemResponseDto> cadastrarImagem(@RequestBody ProdutoImagemRequestDto produtoImagemDto) {
        ProdutoImagem imagem = produtoImagemMapper.toEntity(produtoImagemDto);
        ProdutoImagem imagemCadastrada = produtoImagemService.cadastrarImagem(imagem);
        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagemCadastrada);
        return ResponseEntity.status(201).body(imagemResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponseDto> buscarImagemPorId(@PathVariable Integer id) {
        ProdutoImagem imagem = produtoImagemService.buscarImagemPorId(id);
        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagem);
        return ResponseEntity.ok(imagemResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponseDto> atualizarImagem(@PathVariable Integer id, @RequestBody ProdutoImagemPatchDto produtoImagemDto) {
        ProdutoImagem imagemAtualizada = produtoImagemMapper.toEntity(produtoImagemDto);
        ProdutoImagem imagem = produtoImagemService.atualizarImagem(id, imagemAtualizada);
        ProdutoImagemResponseDto imagemResponse = produtoImagemMapper.toResponseDto(imagem);
        return ResponseEntity.ok(imagemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImagem(@PathVariable Integer id) {
        produtoImagemService.deletarImagem(id);
        return ResponseEntity.noContent().build();
    }
}
