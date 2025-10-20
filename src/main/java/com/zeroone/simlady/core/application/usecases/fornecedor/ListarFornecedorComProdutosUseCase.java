package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.application.ports.ProdutoRepository;

public class ListarFornecedorComProdutosUseCase {
    private final FornecedorRepositoryPort fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public ListarFornecedorComProdutosUseCase(FornecedorRepositoryPort fornecedorRepository, ProdutoRepository produtoRepository) {

        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }
}
