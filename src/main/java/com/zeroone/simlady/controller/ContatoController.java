package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.contato.ContatoRequestDto;
import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.mapper.ContatoMapper;
import com.zeroone.simlady.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contatos", description = "Telefone de Contato com Clientes")
public class ContatoController {

    private final ContatoService contatoService;

    private final ContatoMapper contatoMapper;

    @Operation(summary = "Cadastrar um contato", description = "Cadastra um novo contato de um determinado cliente")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContatoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre usuários",
                    content = @Content()),
    })
    @PostMapping("/{clienteId}")
    public ResponseEntity<ContatoResponseDto> adicionar(@PathVariable Integer clienteId,
                                                        @Valid @RequestBody ContatoRequestDto dto) {
        Contato contato = contatoMapper.toEntity(dto);
        Contato salvo = contatoService.adicionar(clienteId, contato);
        return ResponseEntity.status(201).body(contatoMapper.toDto(salvo));
    }

    @Operation(summary = "Buscar contato por id", description = "Busca um contato pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContatoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content()),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> buscar(@PathVariable Integer id) {
        Contato contato = contatoService.buscar(id);
        return ResponseEntity.ok(contatoMapper.toDto(contato));
    }

    @Operation(summary = "Buscar contato por id do cliente", description = "Busca um contato pelo id do cliente, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato do cliente encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContatoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Contato do cliente não encontrado",
                    content = @Content()),
    })
    @GetMapping("/cliente/{id}")
    public ResponseEntity<Set<ContatoResponseDto>> buscarPorCliente(@PathVariable Integer id) {
        Set<Contato> contatos = contatoService.buscarPorCliente(id);

        Set<ContatoResponseDto> dtos = contatos.stream()
                .map(contatoMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Atualiza um contato", description = "Atualiza um contato pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContatoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre usuários",
                    content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> atualizar(@PathVariable Integer id,
                                                        @Valid @RequestBody ContatoRequestDto dto) {
        Contato contato = contatoMapper.toEntity(dto);
        Contato atualizado = contatoService.atualizar(id, contato);
        return ResponseEntity.ok(contatoMapper.toDto(atualizado));
    }

    @Operation(summary = "Deleta um contato", description = "Deleta um contato pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contato deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable Integer id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

