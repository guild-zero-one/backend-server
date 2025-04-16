package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.fornecedor.FornecedorRequestDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fornecedores")
public class FornecedorController {
    private final FornecedorService fornecedorService;

    private final FornecedorMapper fornecedorMapper;

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDto>> listar() {
        List<FornecedorResponseDto>fornecedores = fornecedorService.listar().stream().map(fornecedorMapper::toResponseDto).toList();
        if (fornecedores.isEmpty()){
            throw new ResourceNotFoundException("Fornecedores não encontrados");
        }
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> buscarPorId(@PathVariable Integer id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        if(fornecedor == null){
            throw new ResourceNotFoundException("Fornecedor não encontrado");
        }
        return ResponseEntity.ok(fornecedorMapper.toResponseDto(fornecedor));
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDto> cadastrarFornecedor(@Valid @RequestBody FornecedorRequestDto dto) {
        Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
        FornecedorResponseDto response = fornecedorMapper.toResponseDto(fornecedorService.cadastrarFornecedor(fornecedor));
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id) {
        fornecedorService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> atualizar(@PathVariable Integer id, @RequestBody FornecedorRequestDto dto){
        Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
        FornecedorResponseDto response = fornecedorMapper.toResponseDto(fornecedorService.atualizar(id,fornecedor));
        return ResponseEntity.status(200).body(response);
    }
}
