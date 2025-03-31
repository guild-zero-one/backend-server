package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.LoteProdutoDTO;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.LoteProdutoMapper;
import com.zeroone.simlady.entity.LoteProduto;
import com.zeroone.simlady.service.LoteProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lotes")
public class LoteProdutoController {

    @Autowired
    private LoteProdutoService loteProdutoService;

    @GetMapping
    public ResponseEntity<List<LoteProdutoDTO>> listar() {
        List<LoteProdutoDTO> lotes = loteProdutoService.listar();
        if (lotes.isEmpty()){
            throw new ResourceNotFoundException("Lotes não encontrados");
        }
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO> buscarPorID(@PathVariable Integer id) {
        LoteProduto loteProduto = loteProdutoService.buscarPorId(id);
        return ResponseEntity.ok(LoteProdutoMapper.toDTO(loteProduto));
    }

    @PostMapping
    public ResponseEntity<LoteProdutoDTO> cadastrarLote(@RequestBody LoteProdutoDTO loteProdutoDTO) {
        loteProdutoDTO.setId(null);
        return ResponseEntity.ok(loteProdutoService.cadastrarLote(loteProdutoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id) {
        loteProdutoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO> atualizarLote(@PathVariable Integer id, @RequestBody LoteProdutoDTO loteProdutoDTO) {
        return ResponseEntity.ok(loteProdutoService.atualizar(id,loteProdutoDTO));
    }
}
