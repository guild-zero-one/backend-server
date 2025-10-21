package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;

public class BuscarProdutoPorSkuUseCase {
    private final ProdutoRepository repository;

    public BuscarProdutoPorSkuUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(String sku) {
        return repository.buscarPorSku(sku).orElseThrow(() -> new RuntimeException("Produto não encontrado com SKU: " + sku));
    }
}
