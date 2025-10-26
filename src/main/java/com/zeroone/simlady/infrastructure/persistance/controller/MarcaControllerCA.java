package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.marca.MarcaRequestDto;
import com.zeroone.simlady.core.adapters.dtos.marca.MarcaResponseDto;
import com.zeroone.simlady.core.application.usecases.marca.*;
import com.zeroone.simlady.core.domain.marca.Marca;
import com.zeroone.simlady.infrastructure.persistance.mapper.MarcaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
@Tag(name = "MarcasCA", description = "API para gerenciamento de marcas")
public class MarcaControllerCA {

    private final CriarMarcaUseCase criarMarcaUseCase;
    private final AtualizarMarcaUseCase atualizarMarcaUseCase;
    private final BuscarMarcaPorIdUseCase buscarMarcaPorIdUseCase;
    private final ListarMarcaUseCase listarMarcaUseCase;
    private final DeletarMarcaPorIdUseCase deletarMarcaPorIdUseCase;

    @PostMapping
    @Operation(summary = "Criar nova marca", description = "Cria uma nova marca no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MarcaResponseDto> criarMarca(@Valid @RequestBody MarcaRequestDto request){
        Marca domain = MarcaMapper.toRawMarca(request);
        Marca marca = criarMarcaUseCase.executar(domain);
        MarcaResponseDto response = MarcaMapper.toResponseDto(marca);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar marca", description = "Atualiza os dados de uma marca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MarcaResponseDto> atualizarMarca(
            @Parameter(description = "ID único da marca") @PathVariable UUID id, 
            @Valid @RequestBody MarcaRequestDto request){
        Marca domain =
                Marca.of(
                        id,
                        request.nome(),
                        request.descricao(),
                        request.imagemUrl(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
        Marca marca = atualizarMarcaUseCase.executar(domain);
        MarcaResponseDto response = MarcaMapper.toResponseDto(marca);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID", description = "Retorna os dados de uma marca específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<MarcaResponseDto> buscarMarcaPorId(
            @Parameter(description = "ID único da marca") @PathVariable UUID id){
        Marca marca = buscarMarcaPorIdUseCase.executar(id);
        MarcaResponseDto response = MarcaMapper.toResponseDto(marca);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar marcas", description = "Retorna uma lista paginada de marcas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de marcas retornada com sucesso")
    })
    public ResponseEntity<Page<MarcaResponseDto>> listarMarcas(
            @Parameter(description = "Número da página (inicia em 0)") @RequestParam(value = "pagina", required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(value = "tamanho", required = false, defaultValue = "10") int size){
        Page<Marca> marcas = listarMarcaUseCase.executar(page, size);
        Page<MarcaResponseDto> response = marcas.map(MarcaMapper::toResponseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar marca", description = "Remove uma marca do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marca deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<Void> deletarMarcaPorId(
            @Parameter(description = "ID único da marca") @PathVariable UUID id){
        deletarMarcaPorIdUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

}
