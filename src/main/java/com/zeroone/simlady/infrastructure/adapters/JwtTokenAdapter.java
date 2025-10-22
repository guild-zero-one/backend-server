package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.GeradorTokenJwtPort;
import com.zeroone.simlady.core.application.usecases.security.GerarTokenUseCase;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements GeradorTokenJwtPort {
    
    private final GerarTokenUseCase gerarTokenUseCase;

    @Override
    public String gerarToken(Usuario usuario, HttpServletResponse response) {
        return gerarTokenUseCase.executar(usuario, response);
    }
}
