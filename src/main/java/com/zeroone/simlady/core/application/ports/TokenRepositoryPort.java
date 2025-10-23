package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.usuario.Usuario;

import java.util.UUID;

public interface TokenRepositoryPort {
    
    String gerarToken(Usuario usuario);
    
    UUID extrairIdUsuario(String token);
    
    boolean validarToken(String token);
}
