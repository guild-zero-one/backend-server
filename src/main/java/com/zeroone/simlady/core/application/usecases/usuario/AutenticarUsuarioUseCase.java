package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenAdapter;
import com.zeroone.simlady.infrastructure.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;

public class AutenticarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;
    private final JwtTokenAdapter geradorToken;

    public AutenticarUsuarioUseCase(UsuarioRepositoryPort repository, JwtTokenAdapter geradorToken) {
        this.repository = repository;
        this.geradorToken = geradorToken;
    }

    public String executar(Usuario usuario, HttpServletResponse response) {
        Usuario usuarioEncontrado = repository.buscarPorEmail(usuario.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        if (!usuarioEncontrado.isAtivo()) {
            throw new UnauthorizedException("Usuário inativo");
        }

        // Aqui você pode adicionar validação de senha com hash
        // Por enquanto, vamos assumir que a validação já foi feita
        
        return geradorToken.gerarToken(usuarioEncontrado, response);
    }
}
