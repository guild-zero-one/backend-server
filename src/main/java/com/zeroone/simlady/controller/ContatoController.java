package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.contato.ContatoRequestDto;
import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.mapper.ContatoMapper;
import com.zeroone.simlady.service.ContatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.CollectionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private final ContatoService contatoService;
    private final ContatoMapper contatoMapper;


    @PostMapping("/{clienteId}")
    public ResponseEntity<ContatoResponseDto> adicionar(@PathVariable Integer clienteId,
                                                        @Valid @RequestBody ContatoRequestDto dto) {
        Contato contato = contatoMapper.toEntity(dto);
        Contato salvo = contatoService.adicionar(clienteId, contato);
        return ResponseEntity.status(201).body(contatoMapper.toDto(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> buscar(@PathVariable Integer id) {
        Contato contato = contatoService.buscar(id);
        return ResponseEntity.ok(contatoMapper.toDto(contato));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<Set<ContatoResponseDto>> buscarPorCliente(@PathVariable Integer id) {
        Set<Contato> contatos = contatoService.buscarPorCliente(id);
        Set<ContatoResponseDto> dtos = contatos.stream()
                .map(contatoMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> atualizar(@PathVariable Integer id,
                                                        @Valid @RequestBody ContatoRequestDto dto) {
        Contato contato = contatoMapper.toEntity(dto);
        Contato atualizado = contatoService.atualizar(id, contato);
        return ResponseEntity.ok(contatoMapper.toDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable Integer id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

