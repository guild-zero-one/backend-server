package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.usuario.UsuarioLoginDto;
import com.zeroone.simlady.dto.usuario.UsuarioRequestDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioTokenDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.mapper.UsuarioMapper;
import com.zeroone.simlady.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping()
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto) {

        Usuario usuario = usuarioMapper.toEntity(dto);

        return ResponseEntity
                .status(201)
                .body(usuarioMapper
                        .toDto(usuarioService
                                .cadastrar(usuario)));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {

        final Usuario usuario = usuarioMapper.toEntity(usuarioLoginDto);

        UsuarioTokenDto usuarioTokenDto = usuarioMapper.toTokenDto(usuario);

        String token = usuarioService.autenticar(usuario);

        usuarioTokenDto.setToken(token);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }


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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscar(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscar(id);
        return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDto dto) {
       Usuario usuario = usuarioMapper.toEntity(dto);

        usuarioService
                .atualizar(id, usuario);

       return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        usuarioService.desativar(id);
         return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
