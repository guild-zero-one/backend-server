package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.venda.VendaRequestDto;
import com.zeroone.simlady.dto.venda.VendaResponseDto;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.mapper.VendaMapper;
import com.zeroone.simlady.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final VendaMapper vendaMapper;

    @PostMapping
    public ResponseEntity<VendaResponseDto> cadastrar(@Valid @RequestBody VendaRequestDto dto) {

        Venda venda = vendaMapper.toEntity(dto);

        vendaService.cadastrar(venda, dto.getPedidos());

        return ResponseEntity.status(201).body(vendaMapper.toDto(venda));

    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDto>> listar() {
        List<Venda> vendas = vendaService.listar();

        if(vendas.isEmpty()) {
            return ResponseEntity
                    .status(204)
                    .build();
        }
        return ResponseEntity
                .ok(vendaMapper
                        .toDto(vendas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> buscar(@PathVariable Integer id) {
        return ResponseEntity
                .ok(vendaMapper
                        .toDto(vendaService
                                .buscar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        vendaService.deletar(id);
        return ResponseEntity
                .status(200)
                .build();
    }


}
