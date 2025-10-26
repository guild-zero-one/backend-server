package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import org.springframework.data.domain.Page;

public class ListarUsuariosPaginadoUseCase {
    private final UsuarioRepositoryPort repository;

    public ListarUsuariosPaginadoUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Usuario> executar(int pagina, int tamanho) {
        return repository.listarTodos(pagina, tamanho);
    }
}
