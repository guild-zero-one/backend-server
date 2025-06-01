package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.dto.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas-por-produto")
    public ResponseEntity<List<ProdutosMaisVendidosResponseDto>> getVendasPorProduto() {
        List<ProdutosMaisVendidosResponseDto> relatorio = relatorioService.listarVendasPorProduto();
        return ResponseEntity.ok(relatorio);
    }

    @Operation(summary = "Resumo de vendas por produto",
            description = "Retorna o total de vendas do mês atual e acumulado para um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoVendasProdutoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content())
    })
    @GetMapping("/vendas-produto/{id}")
    public ResponseEntity<ResumoVendasProdutoResponseDto> getResumoVendasProduto(@PathVariable Integer id) {
        ResumoVendasProdutoResponseDto resumo = relatorioService.obterResumoVendasProduto(id);
        return ResponseEntity.ok(resumo);
    }
}
