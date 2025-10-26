package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class BuscarUsuarioPorIdUseCase {
    private final UsuarioRepositoryPort repository;

    public BuscarUsuarioPorIdUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }
}
