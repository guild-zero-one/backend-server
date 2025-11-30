package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.relatorio.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.core.adapters.dtos.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.core.application.usecases.relatorio.*;
import com.zeroone.simlady.infrastructure.persistance.mapper.RelatorioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios/Dashboard")
public class RelatorioController {

    private final ListarProdutosMaisVendidosUseCase listarProdutosMaisVendidosUseCase;
    private final ListarTop3ProdutosMaisVendidosMesAtualUseCase listarTop3ProdutosMaisVendidosMesAtualUseCase;
    private final ObterResumoVendasProdutoUseCase obterResumoVendasProdutoUseCase;
    private final CalcularTotalVendasMesAtualUseCase calcularTotalVendasMesAtualUseCase;
    private final BuscarTop3ProdutosMaisVendidosMesAtualUseCase buscarTop3ProdutosMaisVendidosMesAtualUseCase;
    private final ObterQuantidadePedidosUltimos6MesesUseCase obterQuantidadePedidosUltimos6MesesUseCase;
    private final ObterFaturamentoUltimos6MesesUseCase obterFaturamentoUltimos6MesesUseCase;
    private final ObterFaturamentoUltimos4MesesUseCase obterFaturamentoUltimos4MesesUseCase;
    private final ObterPedidosPorStatusMesAtualUseCase obterPedidosPorStatusMesAtualUseCase;
    private final ContarPedidosEmAbertoUseCase contarPedidosEmAbertoUseCase;

    @Operation(summary = "Listar vendas por produto",
            description = "Retorna uma lista dos produtos mais vendidos com suas quantidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutosMaisVendidosResponseDto.class))))
    })
    @GetMapping("/vendas-por-produto")
    public ResponseEntity<List<ProdutosMaisVendidosResponseDto>> getVendasPorProduto() {
        List<ProdutosMaisVendidosResponseDto> relatorio = listarProdutosMaisVendidosUseCase.executar().stream()
                .map(RelatorioMapper::toDto)
                .toList();
        return ResponseEntity.ok(relatorio);
    }

    @Operation(summary = "Top 3 produtos mais vendidos do mês atual",
            description = "Retorna os 3 produtos mais vendidos do mês atual com quantidade e valor total")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutosMaisVendidosResponseDto.class))))
    })
    @GetMapping("/top3-produtos-mes-atual")
    public ResponseEntity<List<ProdutosMaisVendidosResponseDto>> getTop3ProdutosMesAtual() {
        List<ProdutosMaisVendidosResponseDto> produtos = listarTop3ProdutosMaisVendidosMesAtualUseCase.executar().stream()
                .map(RelatorioMapper::toDto)
                .toList();
        return ResponseEntity.ok(produtos);
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
    public ResponseEntity<ResumoVendasProdutoResponseDto> getResumoVendasProduto(@PathVariable UUID id) {
        ResumoVendasProdutoResponseDto resumo = RelatorioMapper.toDto(obterResumoVendasProdutoUseCase.executar(id));
        return ResponseEntity.ok(resumo);
    }

    @Operation(summary = "Total de vendas do mês atual",
            description = "Retorna o valor total das vendas realizadas no mês atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)))
    })
    @GetMapping("/total-vendas-mensal")
    public ResponseEntity<BigDecimal> totalVendasMensal() {
        BigDecimal total = calcularTotalVendasMesAtualUseCase.executar();
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Top 3 produtos mais vendidos do mês",
            description = "Retorna os nomes dos 3 produtos mais vendidos no mês atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de nomes retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class))))
    })
    @GetMapping("/top3-produtos-mes")
    public ResponseEntity<List<String>> getTop3NomesProdutosMaisVendidosMesAtual() {
        List<String> nomes = buscarTop3ProdutosMaisVendidosMesAtualUseCase.executar();
        return ResponseEntity.ok(nomes);
    }

    @Operation(summary = "Quantidade de produtos vendidos nos últimos 6 meses",
            description = "Retorna a soma da quantidade de produtos vendidos nos últimos 6 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/quantidade-pedidos-ultimos-6-meses")
    public ResponseEntity<Map<String, Integer>> getQuantidadePedidosUltimos6Meses() {
        Map<String, Integer> quantidade = obterQuantidadePedidosUltimos6MesesUseCase.executar();
        return ResponseEntity.ok(quantidade);
    }

    @Operation(summary = "Valores totais de vendas dos últimos 6 meses",
            description = "Retorna uma lista com o valor total vendido em cada um dos últimos 6 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valores retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BigDecimal.class))))
    })
    @GetMapping("/faturamento-ultimos-6-meses")
    public ResponseEntity<Map<String, BigDecimal>> getFaturamentoUltimos6Meses() {
        Map<String, BigDecimal> faturamento = obterFaturamentoUltimos6MesesUseCase.executar();
        return ResponseEntity.ok(faturamento);
    }

    @Operation(summary = "Valores totais de vendas dos últimos 4 meses",
            description = "Retorna uma lista com o valor total vendido em cada um dos últimos 4 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valores retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BigDecimal.class))))
    })
    @GetMapping("/faturamento-ultimos-4-meses")
    public ResponseEntity<Map<String, BigDecimal>> getFaturamentoUltimos4Meses() {
        Map<String, BigDecimal> faturamento = obterFaturamentoUltimos4MesesUseCase.executar();
        return ResponseEntity.ok(faturamento);
    }

    @Operation(summary = "Total de pedidos por status no mês atual",
            description = "Retorna a quantidade de pedidos agrupados por status no mês atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidades retornadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/pedidos-por-status-mes-atual")
    public ResponseEntity<Map<String, Integer>> getPedidosPorStatusMesAtual() {
        Map<com.zeroone.simlady.core.domain.pedido.StatusPedido, Integer> pedidosPorStatus = 
                obterPedidosPorStatusMesAtualUseCase.executar();
        
        // Converter StatusPedido enum para String para o JSON
        Map<String, Integer> response = new java.util.HashMap<>();
        pedidosPorStatus.forEach((status, quantidade) -> 
                response.put(status.name(), quantidade));
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Quantidade de pedidos em aberto",
            description = "Retorna a quantidade de pedidos com status em aberto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/quantidade-pedidos-em-aberto")
    public ResponseEntity<Integer> getQuantidadePedidosEmAberto() {
        Integer quantidade = contarPedidosEmAbertoUseCase.executar();
        return ResponseEntity.ok(quantidade);
    }
}
