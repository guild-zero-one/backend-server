package com.zeroone.simlady.core.application.ports;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AutenticacaoRepositoryPort {
    UserDetails carregarUsuarioPorUsername(String username);
    Authentication autenticar(String username, String password);
}
