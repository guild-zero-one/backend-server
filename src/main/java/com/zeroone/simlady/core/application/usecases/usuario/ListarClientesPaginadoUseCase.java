package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.usuario.Permissao;
import org.springframework.data.domain.Page;

public class ListarClientesPaginadoUseCase {
    private final UsuarioRepositoryPort repository;

    public ListarClientesPaginadoUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Usuario> executar(int pagina, int tamanho) {
        return repository.listarPorPermissao(Permissao.COMUM, pagina, tamanho);
    }
}
