package com.zeroone.simlady.core.application.usecases.produto;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;

import java.util.UUID;

public class BuscarProdutoPorIdUseCase {

    private final ProdutoRepository repository;

    public BuscarProdutoPorIdUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(UUID id) {
        return repository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }
}
