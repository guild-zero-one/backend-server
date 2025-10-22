package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

import java.util.UUID;

public class AtualizarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;

    public AtualizarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(UUID id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Atualiza apenas os campos fornecidos
        usuarioExistente.atualizarDados(
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getSobrenome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getCelular()
        );

        return repository.atualizarUsuario(usuarioExistente);
    }
}
