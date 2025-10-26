package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

public class BuscarUsuarioPorEmailUseCase {
    private final UsuarioRepositoryPort repository;

    public BuscarUsuarioPorEmailUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(String email) {
        return repository.buscarPorEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
    }
}
