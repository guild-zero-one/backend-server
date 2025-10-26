package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class DeletarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;

    public DeletarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        // Verifica se o usuário existe antes de deletar
        repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        repository.deletarPorId(id);
    }
}
