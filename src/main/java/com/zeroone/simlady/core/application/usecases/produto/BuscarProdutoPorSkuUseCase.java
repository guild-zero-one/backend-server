package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepositoryPort;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.exception.ResourceNotFoundException;

public class BuscarProdutoPorSkuUseCase {
    private final ProdutoRepositoryPort repository;

    public BuscarProdutoPorSkuUseCase(ProdutoRepositoryPort repository) {
        this.repository = repository;
    }

    public Produto executar(String sku) {
        return repository.buscarPorSku(sku).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com SKU: " + sku));
    }
}
