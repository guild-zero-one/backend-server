package com.zeroone.simlady.core.application.ports;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface ExtratorTokenJwtPort {
    UUID extrairIdUsuario(HttpServletRequest request);
}
