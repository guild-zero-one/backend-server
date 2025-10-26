package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.infrastructure.persistance.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VendaRepositoryImpl extends JpaRepository<VendaEntity, UUID> {
    
    @Query("SELECT v FROM VendaEntity v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    Page<VendaEntity> findByDataVendaBetween(@Param("dataInicio") LocalDate dataInicio, 
                                           @Param("dataFim") LocalDate dataFim, 
                                           Pageable pageable);
    
    Page<VendaEntity> findByPagamentoRealizado(Boolean pagamentoRealizado, Pageable pageable);

    // Métodos para relatórios
    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM VendaEntity v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal sumValorTotalByDataVendaBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
    SELECT pi.idProduto
    FROM PedidoItemEntity pi
    JOIN pi.pedido p
    JOIN VendaEntity v ON p.idVenda = v.id
    WHERE v.dataVenda BETWEEN :start AND :end
    GROUP BY pi.idProduto
    ORDER BY SUM(pi.quantidade) DESC
    """)
    List<UUID> findTop3NomesProdutosMaisVendidosNoMes(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // Consultas de debug para verificar dados
    @Query("SELECT COUNT(v) FROM VendaEntity v WHERE v.dataVenda BETWEEN :start AND :end")
    Long countVendasNoPeriodo(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT COUNT(p) FROM PedidoEntity p WHERE p.idVenda IS NOT NULL")
    Long countPedidosComVenda();
    
    @Query("SELECT COUNT(pi) FROM PedidoItemEntity pi JOIN pi.pedido p WHERE p.idVenda IS NOT NULL")
    Long countPedidoItemsComVenda();

    @Query("""
    SELECT\s
        FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') as mesAno,
        COALESCE(SUM(v.valorTotal), 0) as total
    FROM VendaEntity v
    WHERE v.dataVenda >= :start
    GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') DESC
""")
    List<Object[]> sumValorTotalPorMesUltimos6Meses(@Param("start") LocalDate start);

    @Query("""
    SELECT\s
      FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') as mesAno,
      COUNT(v.id) as quantidade
    FROM VendaEntity v
    WHERE v.dataVenda >= :start
    GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') DESC
""")
    List<Object[]> countPedidosPorMesUltimos6Meses(@Param("start") LocalDate start);
}
