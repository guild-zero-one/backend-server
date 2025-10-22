package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceAlreadyExistsException;

public class CadastrarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;

    public CadastrarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(Usuario usuario) {
        // Verifica se já existe um usuário com o mesmo email
        if (repository.buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Usuário já existe com o email: " + usuario.getEmail());
        }

        return repository.salvarUsuario(usuario);
    }
}
