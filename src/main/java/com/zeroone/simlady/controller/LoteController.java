package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.lote.LoteRequestDto;
import com.zeroone.simlady.dto.lote.LoteResponseDto;
import com.zeroone.simlady.dto.lote.LoteResponseItemDto;
import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.mapper.LoteItemMapper;
import com.zeroone.simlady.mapper.LoteMapper;
import com.zeroone.simlady.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
public class LoteController {
    private final LoteService loteService;

    private final LoteMapper loteMapper;

    private final LoteItemMapper loteItemMapper;

    @GetMapping
    public ResponseEntity<List<LoteResponseDto>> listarSemItems() {
        return ResponseEntity.ok(loteService.listar().stream()
                .map(loteMapper::toResponseDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDto> buscarPorId(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id);
        return ResponseEntity.ok(loteMapper.toResponseDto(lote));
    }

    @PostMapping
    public ResponseEntity<LoteResponseItemDto> cadastrarLote(@RequestBody LoteRequestDto loteDto) {
        Lote lote = loteMapper.toEntity(loteDto);
        List<LoteItemRequestDto> loteItems = loteDto.getLoteItems();
        Lote loteCadastrado = loteService.cadastrarLote(lote);
        List<LoteItemRequestDto> loteItemsComId = loteItems.stream().map(loteItemRequestDto -> {
            loteItemRequestDto.setLoteId(loteCadastrado.getId());
            return loteItemRequestDto;
        }).toList();
        List<LoteItem> loteItemsParaSalvar = loteItemsComId.stream()
                .map(loteItemMapper::toEntity)
                .toList();
        List<LoteItem> loteItemCadastrados = loteService.cadastrarLoteItem(loteItemsParaSalvar);
        return ResponseEntity.ok(loteMapper.toResponseItemDto(loteCadastrado, loteItemCadastrados));
    }
}
