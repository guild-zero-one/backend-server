package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.produto.Produto;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepositoryPort {
    Produto salvarProduto(Produto produto);
    Optional<Produto> buscarPorId(UUID id);
    void deletarPorId(UUID id);
    Produto atualizarProduto(Produto produto);
    Page<Produto> listarTodos(int pagina, int tamanho);
    Optional<Produto> buscarPorSku(String sku);
    Page<Produto> listarPorMarca(UUID idMarca, int pagina, int tamanho);
    Page<Produto> listarPorCatalogo(Boolean catalogo, int pagina, int tamanho);
}
