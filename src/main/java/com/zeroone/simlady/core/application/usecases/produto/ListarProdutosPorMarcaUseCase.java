package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public class ListarProdutosPorMarcaUseCase {
    private final ProdutoRepositoryPort repository;

    public ListarProdutosPorMarcaUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Produto> executar(UUID idMarca, int pagina, int tamanho) {
        return repository.listarPorMarca(idMarca, pagina, tamanho);
    }
}
