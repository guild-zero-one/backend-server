package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.fornecedor.FornecedorRequestDto;
import com.zeroone.simlady.core.adapters.dtos.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.core.application.usecases.fornecedor.*;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;
import com.zeroone.simlady.infrastructure.persistance.mapper.FornecedorMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.FornecedorRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorControllerCA {
    private final FornecedorRepositoryImpl repository;

    private final CriarFornecedorUseCase criarFornecedorUseCase;
    private final AtualizarFornecedorUseCase atualizarFornecedorUseCase;
    private final BuscarFornecedorPorIdUseCase buscarFornecedorPorIdUseCase;
    private final ListarFornecedorUseCase listarFornecedorUseCase;
    private final ListarFornecedorComProdutosUseCase listarFornecedorComProdutosUseCase;
    private final DeletarFornecedorPorId deletarFornecedorPorId;


    @PostMapping
    public ResponseEntity<FornecedorResponseDto> criarFornecedor (@RequestBody FornecedorRequestDto request){
        Fornecedor domain = FornecedorMapper.toRawFornecedor(request);
        Fornecedor fornecedor = criarFornecedorUseCase.executar(domain);
        FornecedorResponseDto response = FornecedorMapper.toResponseDto(fornecedor);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<FornecedorResponseDto> atualizarFornecedor (@PathVariable UUID id, @RequestBody FornecedorRequestDto request){
        Fornecedor domain = FornecedorMapper.toRawFornecedor(request);
        domain.setId(id);
        Fornecedor fornecedor = atualizarFornecedorUseCase.executar(domain);
        FornecedorResponseDto response = FornecedorMapper.toResponseDto(fornecedor);
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<FornecedorResponseDto> buscarFornecedorPorId (@PathVariable UUID id){
        Fornecedor fornecedor = buscarFornecedorPorIdUseCase.executar(id);
        FornecedorResponseDto response = FornecedorMapper.toResponseDto(fornecedor);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<FornecedorResponseDto>> listarFornecedores (
            @RequestParam(value = "page" , required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        Page<Fornecedor> fornecedores = listarFornecedorUseCase.executar(page, size);
        Page<FornecedorResponseDto> response = fornecedores.map(FornecedorMapper::toResponseDto);
        return ResponseEntity.ok(response);
    }

//    @GetMapping ("/com-produtos")
//    public ResponseEntity<Page<FornecedorResponseDto>> listarFornecedoresComProdutos (){}

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deletarFornecedorPorId (@PathVariable UUID id){
        deletarFornecedorPorId.executar(id);
        return ResponseEntity.noContent().build();
    }

}
