package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.infrastructure.persistance.entity.PedidoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoItemRepositoryImpl extends JpaRepository<PedidoItemEntity, UUID> {
    
    @Modifying
    @Query("DELETE FROM PedidoItemEntity p WHERE p.pedido.id = :pedidoId")
    void deleteByPedidoId(@Param("pedidoId") UUID pedidoId);

    // A NOVA QUERY CORRIGIDA:
    @Query("""
        SELECT p.id,
            p.nome,
            COALESCE(SUM(pi.quantidade), 0),
            COALESCE(SUM(pi.quantidade * pi.precoUnitario), 0)
        FROM PedidoItemEntity pi
        JOIN pi.pedido pv
        JOIN ProdutoEntity p ON p.id = pi.idProduto
        WHERE pv.status = :status
        GROUP BY p.id, p.nome
        ORDER BY SUM(pi.quantidade) DESC
        """)
    List<Object[]> buscarProdutosMaisVendidosPorStatus(@Param("status") StatusPedido status);

    @Query("""
        SELECT p.id,
            p.nome,
            COALESCE(SUM(pi.quantidade), 0),
            COALESCE(SUM(pi.quantidade * pi.precoUnitario), 0)
        FROM PedidoItemEntity pi
        JOIN pi.pedido pv
        JOIN ProdutoEntity p ON p.id = pi.idProduto
        WHERE pv.status = :status
            AND CAST(pv.criadoEm AS date) >= :inicio
            AND CAST(pv.criadoEm AS date) < :fim
        GROUP BY p.id, p.nome
        ORDER BY SUM(pi.quantidade) DESC
        """)
    List<Object[]> buscarProdutosMaisVendidosPorStatusEPeriodo(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
        SELECT COUNT(pi) FROM PedidoItemEntity pi 
        WHERE pi.idProduto = :produtoId 
        AND pi.pedido.criadoEm BETWEEN :inicio AND :fim
    """)
    Integer countVendasProdutoPeriodo(
            @Param("produtoId") UUID produtoId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("SELECT COUNT(pi) FROM PedidoItemEntity pi WHERE pi.idProduto = :produtoId")
    Integer countVendasTotaisProduto(@Param("produtoId") UUID produtoId);

    // Consultas para debug
    @Query("SELECT COUNT(pi) FROM PedidoItemEntity pi")
    Long countTotalPedidoItems();
    
    @Query("SELECT COUNT(p) FROM PedidoEntity p WHERE p.status = com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO")
    Long countPedidosConcluidos();
    
    @Query("SELECT COUNT(pi) FROM PedidoItemEntity pi JOIN pi.pedido p WHERE p.status = com.zeroone.simlady.core.domain.pedido.StatusPedido.CONCLUIDO")
    Long countPedidoItemsComPedidosConcluidos();
}
