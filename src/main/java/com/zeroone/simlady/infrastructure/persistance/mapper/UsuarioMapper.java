package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.usuario.*;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.persistance.entity.UsuarioEntity;

public class UsuarioMapper {
    
    // Mapeamento Entity <-> Domain
    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Usuario.of(
                entity.getId(),
                entity.getNome(),
                entity.getSobrenome(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getCelular(),
                entity.getAtivo(),
                entity.getPermissao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
    
    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getCelular(),
                usuario.getAtivo(),
                usuario.getPermissao(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }
    
    // Mapeamento Request DTOs -> Domain
    public static Usuario toDomain(UsuarioCreateRequestDto request) {
        if (request == null) {
            return null;
        }
        
        return Usuario.newUsuario(
                request.nome(),
                request.sobrenome(),
                request.email(),
                request.senha(),
                request.celular(),
                request.permissao()
        );
    }
    
    public static Usuario toDomain(UsuarioLoginRequestDto request) {
        if (request == null) {
            return null;
        }
        
        return new Usuario(
                null, // ID será definido pelo sistema
                null, // Nome não é necessário para login
                null, // Sobrenome não é necessário para login
                request.email(),
                request.senha(),
                null, // Celular não é necessário para login
                null, // Ativo será definido pelo sistema
                null, // Permissão será definida pelo sistema
                null, // CriadoEm será definido pelo sistema
                null  // AtualizadoEm será definido pelo sistema
        );
    }
    
    public static Usuario toDomain(UsuarioUpdateRequestDto request) {
        if (request == null) {
            return null;
        }
        
        return new Usuario(
                null, // ID será definido pelo sistema
                request.nome(),
                request.sobrenome(),
                request.email(),
                null, // Senha não é alterada neste DTO
                request.celular(),
                null, // Ativo não é alterado neste DTO
                null, // Permissão não é alterada neste DTO
                null, // CriadoEm não é alterado
                null  // AtualizadoEm será definido pelo sistema
        );
    }
    
    // Mapeamento Domain -> Response DTOs
    public static UsuarioResponseDto toResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getAtivo(),
                usuario.getPermissao(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }
    
    public static UsuarioTokenResponseDto toTokenResponseDto(Usuario usuario, String token) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioTokenResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getAtivo(),
                usuario.getPermissao(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm(),
                token
        );
    }
    
    public static UsuarioClienteResponseDto toClienteResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioClienteResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getCriadoEm()
        );
    }
}
