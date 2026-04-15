package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByFornecedorId(UUID fornecedorId);

    Produto findProdutoBySkuIgnoreCase(String sku);

    Page<Produto> findByFornecedorIdAndNomeContainingIgnoreCase(UUID fornecedorId, String nome, Pageable pageable);

    Page<Produto> findByCategorias_NomeContainingIgnoreCase(String categoriaNome, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT pv.id) FROM PedidoItem pi " +
           "JOIN pi.pedidoVenda pv " +
           "WHERE pi.produto.id = :produtoId")
    Long countDistinctPedidosByProdutoId(@Param("produtoId") UUID produtoId);
}
