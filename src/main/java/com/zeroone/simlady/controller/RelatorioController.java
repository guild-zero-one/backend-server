package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
