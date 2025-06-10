package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {

    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal sumValorTotalByDataVendaBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
    SELECT pi.produto.nome
    FROM PedidoItem pi
    JOIN pi.pedidoVenda pv
    WHERE pv.venda.dataVenda BETWEEN :start AND :end
    GROUP BY pi.produto.nome
    ORDER BY SUM(pi.quantidade) DESC
    """)
    List<String> findTop3NomesProdutosMaisVendidosNoMes(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
    SELECT COALESCE(SUM(pi.quantidade), 0)
    FROM PedidoItem pi
    JOIN pi.pedidoVenda pv
    WHERE pv.venda.dataVenda BETWEEN :start AND :end
    """)
    Integer quantidadeProdutosVendidosUltimos6Meses(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
    SELECT FUNCTION('TO_CHAR', v.dataVenda, 'Month') as mes, COALESCE(SUM(v.valorTotal), 0) as total
    FROM Venda v
    WHERE v.dataVenda >= :start
    GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'Month'), FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda)
    ORDER BY FUNCTION('YEAR', v.dataVenda) DESC, FUNCTION('MONTH', v.dataVenda) DESC
""")
    List<Object[]> sumValorTotalPorMesUltimos6Meses(@Param("start") LocalDate start);


    @Query("""
    SELECT\s
      FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') as mesAno,
      SUM(SIZE(v.pedidos)) as quantidade
    FROM Venda v
    WHERE v.dataVenda >= :start
    GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') DESC
""")
    List<Object[]> countPedidosPorMesUltimos6Meses(@Param("start") LocalDate start);


}
