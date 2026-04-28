package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.dashboard.HomeKpisResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
