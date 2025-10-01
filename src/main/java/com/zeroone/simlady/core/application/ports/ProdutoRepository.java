package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.produto.Produto;

import java.util.List;

public interface ProdutoRepository {
    Produto salvarProduto(Produto produto);
    List<Produto> listar();
}
