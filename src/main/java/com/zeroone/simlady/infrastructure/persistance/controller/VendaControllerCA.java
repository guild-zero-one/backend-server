package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.venda.VendaCreateRequestDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaResponseDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaUpdateRequestDto;
import com.zeroone.simlady.core.application.usecases.venda.*;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.infrastructure.persistance.mapper.VendaMapper;
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
@RequestMapping("/vendasCA")
@RequiredArgsConstructor
@Tag(name = "VendasCA", description = "API para gerenciamento de vendas")
public class VendaControllerCA {
    
    private final CriarVendaUseCase criarVendaUseCase;
    private final BuscarVendaPorIdUseCase buscarVendaPorIdUseCase;
    private final ListarVendasUseCase listarVendasUseCase;
    private final AtualizarVendaUseCase atualizarVendaUseCase;
    private final DeletarVendaPorIdUseCase deletarVendaPorIdUseCase;
    private final ConfirmarPagamentoVendaUseCase confirmarPagamentoVendaUseCase;
    private final VendaMapper vendaMapper;
    
    @PostMapping
    @Operation(summary = "Criar nova venda")
    public ResponseEntity<VendaResponseDto> criarVenda(@Valid @RequestBody VendaCreateRequestDto request) {
        Venda venda = criarVendaUseCase.executar(
                request.getValorTotal(),
                request.getDesconto(),
                request.getDataVenda(),
                request.getPedidosIds()
        );
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID")
    public ResponseEntity<VendaResponseDto> buscarVendaPorId(@PathVariable UUID id) {
        Optional<Venda> venda = buscarVendaPorIdUseCase.executar(id);
        
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda.get());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Listar todas as vendas")
    public ResponseEntity<Page<VendaResponseDto>> listarVendas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Venda> vendas = listarVendasUseCase.executar(pagina, tamanho);
        Page<VendaResponseDto> response = vendas.map(vendaMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar venda")
    public ResponseEntity<VendaResponseDto> atualizarVenda(
            @PathVariable UUID id,
            @Valid @RequestBody VendaUpdateRequestDto request) {
        
        Optional<Venda> venda = atualizarVendaUseCase.executar(
                id,
                request.getValorTotal(),
                request.getDesconto(),
                request.getDataVenda(),
                request.getPedidosIds()
        );
        
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda.get());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar venda")
    public ResponseEntity<Void> deletarVenda(@PathVariable UUID id) {
        deletarVendaPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/confirmar-pagamento")
    @Operation(summary = "Confirmar pagamento da venda")
    public ResponseEntity<VendaResponseDto> confirmarPagamentoVenda(@PathVariable UUID id) {
        Optional<Venda> venda = confirmarPagamentoVendaUseCase.executar(id);
        
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda.get());
        return ResponseEntity.ok(response);
    }
}
