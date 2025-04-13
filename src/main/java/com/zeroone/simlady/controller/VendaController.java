package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.venda.VendaRequestDto;
import com.zeroone.simlady.dto.venda.VendaResponseDto;
import com.zeroone.simlady.entity.Venda;
import com.zeroone.simlady.mapper.VendaMapper;
import com.zeroone.simlady.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
