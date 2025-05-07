package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.usuario.UsuarioLoginDto;
import com.zeroone.simlady.dto.usuario.UsuarioRequestDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioTokenDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.exception.UnauthorizedException;
import com.zeroone.simlady.mapper.UsuarioMapper;
import com.zeroone.simlady.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Clientes e Administradores")
public class UsuarioController {
    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    @Operation(summary = "Cadastrar usuários", description = "Cadastra usuário sendo clientes ou administradores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content())
    })
    @PostMapping()
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);

        return ResponseEntity
                .status(201)
                .body(usuarioMapper
                        .toDto(usuarioService
                                .cadastrar(usuario)));
    }

    @Operation(summary = "Login de usuários", description = "Realiza a autenticação de um usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioTokenDto.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content())
    })

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(
            @RequestBody UsuarioLoginDto usuarioLoginDto, HttpServletResponse response) {

        Usuario usuario = usuarioMapper.toEntity(usuarioLoginDto);
        UsuarioTokenDto usuarioTokenDto = usuarioMapper.toTokenDto(usuario);

        String token = usuarioService.autenticar(usuario, response);

        usuarioTokenDto.setToken(token);

        return ResponseEntity.ok(usuarioTokenDto);
    }

    @Operation(summary = "Listar usuários", description = "Lista todos os usuários do sistema")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados na base",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))),
            @ApiResponse(responseCode = "204", description = "Sem usuários na base",
                    content = @Content())
    })

    @GetMapping
    public ResponseEntity <List<UsuarioResponseDto>> listarClientes() {
        List<Usuario> usuarios = usuarioService.listar();

        if(usuarios.isEmpty()) {
            return ResponseEntity
                    .status(204)
                    .build();
        }

        return ResponseEntity
                .status(200)
                .body(usuarios
                        .stream()
                        .map(usuarioMapper::toDto).toList());
    }

    @Operation(summary = "Buscar usuário por id", description = "Busca usuário pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscar(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscar(id);
        return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @Operation(summary = "Atualizar usuário por id", description = "Atualiza usuário pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflito de informações entre usuários",
                    content = @Content())
    })

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDto dto) {
       Usuario usuario = usuarioMapper.toEntity(dto);

        usuarioService
                .atualizar(id, usuario);

       return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @GetMapping("/autenticado")
    public ResponseEntity<UsuarioResponseDto> buscarAutenticado(HttpServletRequest request) {

        return ResponseEntity
                .status(200)
                .body(usuarioMapper
                        .toDto(usuarioService
                                .buscarAutenticado(request)));

    }

    @Operation(summary = "Desativar usuário por id", description = "Desativa usuário pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content()),
    })

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        usuarioService.desativar(id);
         return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar usuário por id", description = "Deleta usuário pelo id, caso exista")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }



}
