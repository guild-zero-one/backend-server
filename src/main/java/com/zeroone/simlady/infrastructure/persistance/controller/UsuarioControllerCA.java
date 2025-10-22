package com.zeroone.simlady.infrastructure.persistance.controller;

import com.zeroone.simlady.core.adapters.dtos.usuario.*;
import com.zeroone.simlady.infrastructure.persistance.mapper.UsuarioMapper;
import com.zeroone.simlady.core.application.usecases.usuario.*;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuariosCA")
@RequiredArgsConstructor
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
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@RequestBody UsuarioCreateRequestDto request) {
        Usuario usuario = UsuarioMapper.toDomain(request);
        Usuario usuarioSalvo = cadastrarUsuarioUseCase.executar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponseDto(usuarioSalvo));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenResponseDto> autenticarUsuario(
            @RequestBody UsuarioLoginRequestDto request,
            HttpServletResponse response) {
        Usuario usuario = UsuarioMapper.toDomain(request);
        String token = autenticarUsuarioUseCase.executar(usuario, response);
        
        // Busca o usuário completo pelo email para retornar no response
        Usuario usuarioCompleto = buscarUsuarioPorEmailUseCase.executar(request.email());
        
        return ResponseEntity.ok(UsuarioMapper.toTokenResponseDto(usuarioCompleto, token));
    }

    @PostMapping("/logout")
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
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioAutenticado(HttpServletRequest request) {
        Usuario usuario = buscarUsuarioAutenticadoUseCase.executar(request);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable UUID id) {
        Usuario usuario = buscarUsuarioPorIdUseCase.executar(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(
            @PathVariable UUID id, 
            @RequestBody UsuarioUpdateRequestDto request) {
        Usuario usuarioAtualizado = UsuarioMapper.toDomain(request);
        Usuario usuarioSalvo = atualizarUsuarioUseCase.executar(id, usuarioAtualizado);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuarioSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable UUID id) {
        desativarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
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
    public ResponseEntity<UsuarioClienteResponseDto> buscarClientePorId(@PathVariable UUID id) {
        Usuario cliente = buscarUsuarioPorIdUseCase.executar(id);
        return ResponseEntity.ok(UsuarioMapper.toClienteResponseDto(cliente));
    }
}
