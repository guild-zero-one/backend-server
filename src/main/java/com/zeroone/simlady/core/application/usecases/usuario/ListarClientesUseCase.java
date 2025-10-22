package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.usuario.Permissao;

import java.util.List;

public class ListarClientesUseCase {
    private final UsuarioRepositoryPort repository;

    public ListarClientesUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Usuario> executar() {
        return repository.listarPorPermissao(Permissao.COMUM);
    }
}
