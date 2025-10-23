package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.springframework.data.domain.Page;

public class ListarProdutosPorCatalogoUseCase {
    private final ProdutoRepositoryPort repository;

    public ListarProdutosPorCatalogoUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Produto> executar(Boolean catalogo, int pagina, int tamanho) {
        return repository.listarPorCatalogo(catalogo, pagina, tamanho);
    }
}
