package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public class ListarProdutosPorFornecedorUseCase {
    private final ProdutoRepository repository;

    public ListarProdutosPorFornecedorUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Page<Produto> executar(UUID idFornecedor, int pagina, int tamanho) {
        return repository.listarPorFornecedor(idFornecedor, pagina, tamanho);
    }
}
