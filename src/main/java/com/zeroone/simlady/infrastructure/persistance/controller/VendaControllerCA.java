package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.venda.VendaCreateRequestDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaResponseDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaUpdateRequestDto;
import com.zeroone.simlady.core.application.usecases.venda.*;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.infrastructure.persistance.mapper.VendaMapper;
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
@RequestMapping("/vendas")
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
    @Operation(summary = "Criar nova venda", description = "Cria uma nova venda no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
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
    @Operation(summary = "Buscar venda por ID", description = "Retorna os dados de uma venda específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<VendaResponseDto> buscarVendaPorId(
            @Parameter(description = "ID único da venda") @PathVariable UUID id) {
        Optional<Venda> venda = buscarVendaPorIdUseCase.executar(id);
        
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda.get());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Listar todas as vendas", description = "Retorna uma lista paginada de vendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vendas retornada com sucesso")
    })
    public ResponseEntity<Page<VendaResponseDto>> listarVendas(
            @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho) {
        
        Page<Venda> vendas = listarVendasUseCase.executar(pagina, tamanho);
        Page<VendaResponseDto> response = vendas.map(vendaMapper::toResponseDto);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar venda", description = "Atualiza os dados de uma venda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<VendaResponseDto> atualizarVenda(
            @Parameter(description = "ID único da venda") @PathVariable UUID id,
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
    @Operation(summary = "Deletar venda", description = "Remove uma venda do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venda deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<Void> deletarVenda(
            @Parameter(description = "ID único da venda") @PathVariable UUID id) {
        deletarVendaPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/confirmar-pagamento")
    @Operation(summary = "Confirmar pagamento da venda", description = "Confirma o pagamento de uma venda específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<VendaResponseDto> confirmarPagamentoVenda(
            @Parameter(description = "ID único da venda") @PathVariable UUID id) {
        Optional<Venda> venda = confirmarPagamentoVendaUseCase.executar(id);
        
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        VendaResponseDto response = vendaMapper.toResponseDto(venda.get());
        return ResponseEntity.ok(response);
    }
}
