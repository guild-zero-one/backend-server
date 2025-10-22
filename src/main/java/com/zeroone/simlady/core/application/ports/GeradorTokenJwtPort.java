package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletResponse;

public interface GeradorTokenJwtPort {
    String gerarToken(Usuario usuario, HttpServletResponse response);
}
