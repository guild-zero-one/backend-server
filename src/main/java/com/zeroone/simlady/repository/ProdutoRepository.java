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

    Page<Produto> findByFornecedorId(UUID fornecedorId, Pageable pageable);

    Long countByFornecedorId(UUID fornecedorId);

    Produto findProdutoBySkuIgnoreCase(String sku);

    Page<Produto> findByFornecedorIdAndNomeContainingIgnoreCase(UUID fornecedorId, String nome, Pageable pageable);

    Page<Produto> findByCategorias_NomeContainingIgnoreCase(String categoriaNome, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT pv.id) FROM PedidoItem pi " +
           "JOIN pi.pedidoVenda pv " +
           "WHERE pi.produto.id = :produtoId")
    Long countDistinctPedidosByProdutoId(@Param("produtoId") UUID produtoId);

    @Query("SELECT COALESCE(SUM(p.quantidade), 0) FROM Produto p")
    Long sumQuantidadeTotalEmEstoque();

    // StatusPedido ordinals: PENDENTE=0, CONCLUIDO=1, CANCELADO=2
    @Query(value = """
        SELECT p.nome,
               COALESCE(SUM(CASE WHEN pv.status IN (0, 1) THEN pi.quantidade ELSE 0 END), 0) AS pedidos,
               p.quantidade AS estoque
        FROM produto p
        LEFT JOIN pedido_item pi ON pi.fk_produto = p.id
        LEFT JOIN pedido_venda pv ON pv.id = pi.fk_pedido
        GROUP BY p.id, p.nome, p.quantidade
        ORDER BY (
            COALESCE(SUM(CASE WHEN pv.status IN (0, 1) THEN pi.quantidade ELSE 0 END), 0)::float
            / NULLIF(p.quantidade, 0)
        ) DESC NULLS LAST
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> findProdutosComDemandaEEstoque();
}
