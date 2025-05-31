package com.zeroone.simlady.repository;

import com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto;
import com.zeroone.simlady.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Integer> {

    @Query("""
    SELECT new com.zeroone.simlady.dto.produto.ProdutosMaisVendidosResponseDto(
        p.id,
        p.nome,
        COALESCE(SUM(pi.quantidade), 0),
        COALESCE(SUM(pi.quantidade * pi.precoUnitario), 0)
    )
    FROM PedidoItem pi
    JOIN pi.produto p
    JOIN pi.pedidoVenda pv
    JOIN pv.venda v
    WHERE pv.status = com.zeroone.simlady.entity.enums.StatusPedido.CONCLUIDO
      AND v.pagamentoRealizado = TRUE
    GROUP BY p.id, p.nome
    ORDER BY SUM(pi.quantidade) DESC
""")
    List<ProdutosMaisVendidosResponseDto> buscarProdutosMaisVendidos();
}
