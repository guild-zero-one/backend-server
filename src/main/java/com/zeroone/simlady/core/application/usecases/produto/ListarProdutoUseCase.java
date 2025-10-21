package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.springframework.data.domain.Page;

public class ListarProdutoUseCase {
    private final ProdutoRepository repository;

    public ListarProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Page<Produto> executar(int pagina, int tamanho) {
        return repository.listarTodos(pagina, tamanho);
    }
}