package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.usuario.*;
import com.zeroone.simlady.infrastructure.persistance.mapper.UsuarioMapper;
import com.zeroone.simlady.core.application.usecases.usuario.*;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "UsuáriosCA", description = "API para gerenciamento de usuários")
public class UsuarioControllerCA {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;
    private final BuscarUsuarioAutenticadoUseCase buscarUsuarioAutenticadoUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final DesativarUsuarioUseCase desativarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final ListarClientesUseCase listarClientesUseCase;

    @PostMapping
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@Valid @RequestBody UsuarioCreateRequestDto request) {
        Usuario usuario = UsuarioMapper.toDomain(request);
        Usuario usuarioSalvo = cadastrarUsuarioUseCase.executar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponseDto(usuarioSalvo));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login do usuário e retorna token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<UsuarioTokenResponseDto> autenticarUsuario(
            @Valid @RequestBody UsuarioLoginRequestDto request,
            HttpServletResponse response) {
        Usuario usuario = UsuarioMapper.toDomain(request);
        String token = autenticarUsuarioUseCase.executar(usuario, response);
        
        // Busca o usuário completo pelo email para retornar no response
        Usuario usuarioCompleto = buscarUsuarioPorEmailUseCase.executar(request.email());
        
        return ResponseEntity.ok(UsuarioMapper.toTokenResponseDto(usuarioCompleto, token));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout do usuário", description = "Realiza logout do usuário removendo o token de autenticação")
    @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/autenticado")
    @Operation(summary = "Buscar usuário autenticado", description = "Retorna os dados do usuário atualmente autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioAutenticado(HttpServletRequest request) {
        Usuario usuario = buscarUsuarioAutenticadoUseCase.executar(request);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDto> buscarPorId(
            @Parameter(description = "ID único do usuário") @PathVariable UUID id) {
        Usuario usuario = buscarUsuarioPorIdUseCase.executar(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(
            @Parameter(description = "ID único do usuário") @PathVariable UUID id, 
            @Valid @RequestBody UsuarioUpdateRequestDto request) {
        Usuario usuarioAtualizado = UsuarioMapper.toDomain(request);
        Usuario usuarioSalvo = atualizarUsuarioUseCase.executar(id, usuarioAtualizado);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuarioSalvo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID único do usuário") @PathVariable UUID id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário sem removê-lo do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> desativarUsuario(
            @Parameter(description = "ID único do usuário") @PathVariable UUID id) {
        desativarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<Usuario> usuarios = listarUsuariosUseCase.executar();
        
        if(usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        List<UsuarioResponseDto> response = usuarios.stream()
                .map(UsuarioMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clientes")
    @Operation(summary = "Listar clientes", description = "Retorna uma lista com todos os clientes do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado")
    })
    public ResponseEntity<List<UsuarioClienteResponseDto>> listarClientes() {
        List<Usuario> clientes = listarClientesUseCase.executar();
        
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<UsuarioClienteResponseDto> response = clientes.stream()
                .map(UsuarioMapper::toClienteResponseDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clientes/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<UsuarioClienteResponseDto> buscarClientePorId(
            @Parameter(description = "ID único do cliente") @PathVariable UUID id) {
        Usuario cliente = buscarUsuarioPorIdUseCase.executar(id);
        return ResponseEntity.ok(UsuarioMapper.toClienteResponseDto(cliente));
    }
}
