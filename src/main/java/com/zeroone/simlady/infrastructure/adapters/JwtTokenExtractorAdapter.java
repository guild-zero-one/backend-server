package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.ExtratorTokenJwtPort;
import com.zeroone.simlady.core.application.usecases.security.ExtrairIdUsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractorAdapter implements ExtratorTokenJwtPort {
    
    private final ExtrairIdUsuarioUseCase extrairIdUsuarioUseCase;

    @Override
    public UUID extrairIdUsuario(HttpServletRequest request) {
        return extrairIdUsuarioUseCase.executar(request);
    }
}
