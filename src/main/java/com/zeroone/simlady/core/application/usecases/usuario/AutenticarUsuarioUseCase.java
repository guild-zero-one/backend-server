package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenAdapter;
import com.zeroone.simlady.infrastructure.security.AutenticarUsuarioSecurityUseCase;
import com.zeroone.simlady.infrastructure.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;

public class AutenticarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;
    private final JwtTokenAdapter geradorToken;
    private final AutenticarUsuarioSecurityUseCase autenticacaoSecurity;

    public AutenticarUsuarioUseCase(UsuarioRepositoryPort repository, JwtTokenAdapter geradorToken, 
                                   AutenticarUsuarioSecurityUseCase autenticacaoSecurity) {
        this.repository = repository;
        this.geradorToken = geradorToken;
        this.autenticacaoSecurity = autenticacaoSecurity;
    }

    public String executar(Usuario usuario, HttpServletResponse response) {
        try {
            // Validar credenciais usando o security use case
            autenticacaoSecurity.executar(usuario.getEmail(), usuario.getSenha());
            
            // Buscar usuário completo
            Usuario usuarioEncontrado = repository.buscarPorEmail(usuario.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

            if (!usuarioEncontrado.isAtivo()) {
                throw new UnauthorizedException("Usuário inativo");
            }
            
            return geradorToken.gerarToken(usuarioEncontrado, response);
        } catch (Exception e) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }
    }
}
