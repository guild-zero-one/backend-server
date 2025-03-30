package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.contato.ContatoRequestDto;
import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.service.ContatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping("/{clienteId}")
    public ResponseEntity<ContatoResponseDto> adicionar(@PathVariable Integer clienteId,
                                                               @Valid @RequestBody ContatoRequestDto dto) {
        return ResponseEntity.status(201)
                .body(contatoService
                        .adicionar(clienteId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> buscar(@PathVariable Integer id) {

        return ResponseEntity
                .ok(contatoService
                        .buscarPorId(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<Set<ContatoResponseDto>> buscarPorCliente(@PathVariable Integer id) {

        return ResponseEntity
                .ok(contatoService
                        .buscarPorCliente(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> atualizar(@PathVariable Integer id,
                                                               @Valid @RequestBody  ContatoRequestDto dto) {
        return ResponseEntity
                .ok(contatoService
                                .atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato (@PathVariable Integer id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
