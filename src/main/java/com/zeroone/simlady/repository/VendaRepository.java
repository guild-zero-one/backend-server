package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {

    @Query("""
    SELECT DISTINCT v FROM Venda v
    LEFT JOIN FETCH v.pedidos pv
    LEFT JOIN FETCH pv.usuario u
    LEFT JOIN FETCH pv.itens pi
    LEFT JOIN FETCH pi.produto
    WHERE v.id = :id
    """)
    Optional<Venda> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
    SELECT DISTINCT v FROM Venda v
    LEFT JOIN FETCH v.pedidos pv
    LEFT JOIN FETCH pv.usuario
    """)
    List<Venda> findAllWithUsuarios();

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
    SELECT\s
        FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') as mesAno,
        COALESCE(SUM(v.valorTotal), 0) as total
    FROM Venda v
    WHERE v.dataVenda >= :start
    GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'YYYY-MM') DESC
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

    @Query(
            value = """
                    SELECT DISTINCT v
                    FROM Venda v
                    LEFT JOIN FETCH v.pedidos p
                    LEFT JOIN FETCH p.usuario
                    """,
            countQuery = "SELECT COUNT(v) FROM Venda v"
    )
    Page<Venda> listarComUsuario(Pageable pageable);

    @Query("""
            SELECT DISTINCT v
            FROM Venda v
            LEFT JOIN FETCH v.pedidos p
            LEFT JOIN FETCH p.usuario
            LEFT JOIN FETCH p.itens i
            LEFT JOIN FETCH i.produto
            WHERE v.id = :id
            """)
    Optional<Venda> buscarDetalhePorId(@Param("id") UUID id);

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.pagamentoRealizado = false")
    Long countVendasPendentesPagamento();

    @Query("""
        SELECT FUNCTION('TO_CHAR', v.dataVenda, 'ID') as dia, COALESCE(SUM(v.valorTotal), 0)
        FROM Venda v
        WHERE v.dataVenda >= :dataInicio
        GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'ID')
        ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'ID') ASC
    """)
    List<Object[]> findFaturamentoPorDiaSemana(@Param("dataInicio") LocalDate dataInicio);

    @Query("""
        SELECT FUNCTION('TO_CHAR', v.dataVenda, 'W') as semana, COALESCE(SUM(v.valorTotal), 0)
        FROM Venda v
        WHERE v.dataVenda >= :dataInicio
        GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'W')
        ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'W') ASC
    """)
    List<Object[]> findFaturamentoPorSemanaMes(@Param("dataInicio") LocalDate dataInicio);

    @Query("""
        SELECT FUNCTION('TO_CHAR', v.dataVenda, 'MM') as mes, COALESCE(SUM(v.valorTotal), 0)
        FROM Venda v
        WHERE v.dataVenda >= :dataInicio
        GROUP BY FUNCTION('TO_CHAR', v.dataVenda, 'MM')
        ORDER BY FUNCTION('TO_CHAR', v.dataVenda, 'MM') ASC
    """)
    List<Object[]> findFaturamentoPorMes(@Param("dataInicio") LocalDate dataInicio);

    @Query("""
        SELECT u.nome, u.sobrenome, v.dataVenda, v.valorTotal
        FROM Venda v
        JOIN v.pedidos p
        JOIN p.usuario u
        WHERE v.pagamentoRealizado = false
        ORDER BY v.dataVenda ASC
    """)
    List<Object[]> findPagamentosPendentes(Pageable pageable);

    @Query("""
        SELECT u.nome, u.sobrenome, SUM(v.valorTotal) as total
        FROM Venda v
        JOIN v.pedidos p
        JOIN p.usuario u
        WHERE p.status = com.zeroone.simlady.entity.enums.StatusPedido.CONCLUIDO
          AND v.dataVenda >= :dataInicio
        GROUP BY u.id, u.nome, u.sobrenome
        ORDER BY total DESC
    """)
    List<Object[]> findRankingCompradores(@Param("dataInicio") LocalDate dataInicio, Pageable pageable);

}


