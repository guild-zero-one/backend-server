package com.zeroone.simlady.core.application.ports;

import org.springframework.security.core.userdetails.UserDetails;

public interface ValidadorTokenJwtPort {
    boolean validarToken(String token, UserDetails userDetails);
    boolean isTokenExpirado(String token);
    String extrairUsername(String token);
}
