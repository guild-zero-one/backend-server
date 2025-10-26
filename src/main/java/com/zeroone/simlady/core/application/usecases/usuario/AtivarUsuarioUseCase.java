package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class AtivarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;

    public AtivarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        Usuario usuario = repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.ativar();
        repository.atualizarUsuario(usuario);
    }
}
