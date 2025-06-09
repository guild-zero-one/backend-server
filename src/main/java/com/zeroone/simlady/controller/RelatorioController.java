package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.dto.relatorio.ResumoVendasProdutoResponseDto;
import com.zeroone.simlady.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Operation(summary = "Listar vendas por produto",
            description = "Retorna uma lista dos produtos mais vendidos com suas quantidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutosMaisVendidosResponseDto.class))))
    })
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

    @Operation(summary = "Total de vendas do mês atual",
            description = "Retorna o valor total das vendas realizadas no mês atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)))
    })
    @GetMapping("/total-vendas-mensal")
    public ResponseEntity<BigDecimal> totalVendasMensal() {
        BigDecimal total = relatorioService.totalVendasMesAtual();
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
        List<String> nomes = relatorioService.top3NomesProdutosMaisVendidosMesAtual();
        return ResponseEntity.ok(nomes);
    }

    @Operation(summary = "Quantidade de produtos vendidos nos últimos 6 meses",
            description = "Retorna a soma da quantidade de produtos vendidos nos últimos 6 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/quantidade-produtos-ultimos-6-meses")
    public ResponseEntity<Integer> getQuantidadeProdutosVendidosUltimos6Meses() {
        Integer quantidade = relatorioService.quantidadeProdutosVendidosUltimos6Meses();
        return ResponseEntity.ok(quantidade);
    }

    @Operation(summary = "Valores totais de vendas dos últimos 6 meses",
            description = "Retorna uma lista com o valor total vendido em cada um dos últimos 6 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valores retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BigDecimal.class))))
    })
    @GetMapping("/valores-vendas-ultimos-6-meses")
    public ResponseEntity<List<BigDecimal>> getValoresVendasUltimos6Meses() {
        List<BigDecimal> valores = relatorioService.valoresVendasUltimos6Meses();
        return ResponseEntity.ok(valores);
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
        Integer quantidade = relatorioService.pedidosEmAberto();
        return ResponseEntity.ok(quantidade);
    }
}