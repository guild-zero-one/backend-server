package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.dashboard.*;
import com.zeroone.simlady.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Indicadores da home do admin")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "KPIs da home", description = "Retorna os indicadores de estoque, pedidos pendentes e vendas pendentes")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPIs retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HomeKpisResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores",
                    content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/home-kpis")
    public ResponseEntity<HomeKpisResponseDto> buscarHomeKpis() {
        return ResponseEntity.ok(dashboardService.obterHomeKpis());
    }

    @Operation(summary = "Faturamento por período", description = "Retorna o faturamento agrupado por dia (7), semana (30) ou mês (90)")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faturamento retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro 'periodo' ausente ou inválido", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/faturamento")
    public ResponseEntity<List<FaturamentoItemDTO>> getFaturamento(@RequestParam String periodo) {
        return ResponseEntity.ok(dashboardService.getFaturamento(periodo));
    }

    @Operation(summary = "Status dos pedidos", description = "Retorna a contagem de pedidos agrupada por status")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retornados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/status-pedidos")
    public ResponseEntity<List<StatusPedidoDTO>> getStatusPedidos() {
        return ResponseEntity.ok(dashboardService.getStatusPedidos());
    }

    @Operation(summary = "Pagamentos pendentes", description = "Lista as vendas com pagamento em aberto (máx. 10), ordenadas da mais antiga")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos retornados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/pagamentos-pendentes")
    public ResponseEntity<List<PagamentoPendenteDTO>> getPagamentosPendentes() {
        return ResponseEntity.ok(dashboardService.getPagamentosPendentes());
    }

    @Operation(summary = "Produtos × Estoque", description = "Lista os 10 produtos com maior risco de estoque (demanda vs disponibilidade)")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/produtos-estoque")
    public ResponseEntity<List<ProdutoEstoqueDTO>> getProdutosEstoque() {
        return ResponseEntity.ok(dashboardService.getProdutosEstoque());
    }

    @Operation(summary = "Ranking de compradores", description = "Retorna os 5 clientes com maior valor total de compras no período")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro 'periodo' ausente ou inválido", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ranking-compradores")
    public ResponseEntity<List<RankingCompradorDTO>> getRankingCompradores(@RequestParam String periodo) {
        return ResponseEntity.ok(dashboardService.getRankingCompradores(periodo));
    }

    @Operation(summary = "Clientes inativos", description = "Lista clientes sem pedidos há pelo menos N dias (30 ou 60)")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro 'diasSemPedido' ausente ou inválido", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a administradores", content = @Content())
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clientes-inativos")
    public ResponseEntity<List<ClienteInativoDTO>> getClientesInativos(@RequestParam String diasSemPedido) {
        return ResponseEntity.ok(dashboardService.getClientesInativos(diasSemPedido));
    }
}
