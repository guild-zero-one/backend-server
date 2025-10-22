package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.application.ports.ExtratorTokenJwtPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class BuscarUsuarioAutenticadoUseCase {
    private final UsuarioRepositoryPort repository;
    private final ExtratorTokenJwtPort extratorToken;

    public BuscarUsuarioAutenticadoUseCase(UsuarioRepositoryPort repository, ExtratorTokenJwtPort extratorToken) {
        this.repository = repository;
        this.extratorToken = extratorToken;
    }

    public Usuario executar(HttpServletRequest request) {
        UUID usuarioId = extratorToken.extrairIdUsuario(request);
        
        return repository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
    }
}
