package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;

import java.util.List;

public class ListarUsuariosUseCase {
    private final UsuarioRepositoryPort repository;

    public ListarUsuariosUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Usuario> executar() {
        return repository.listarTodos();
    }
}
