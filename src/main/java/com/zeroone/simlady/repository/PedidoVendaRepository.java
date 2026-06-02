package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, UUID> {
    List<PedidoVenda> findAllByStatus(StatusPedido status);

    List<PedidoVenda> findAllByUsuario(Usuario usuario);

    Integer countPedidoVendasByUsuario_IdAndStatus(UUID usuarioId, StatusPedido statusPedido);

    Integer countByStatus(StatusPedido statusPedido);

    @Query("SELECT pv FROM PedidoVenda pv WHERE CAST(pv.id AS string) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PedidoVenda> findByIdLike(@Param("search") String search, Pageable pageable);

    Page<PedidoVenda> findByUsuario_NomeContainingIgnoreCase(String usuarioNome, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario", "venda"})
    @Query("""
            SELECT pv
            FROM PedidoVenda pv
            WHERE (:status IS NULL OR pv.status = :status)
            """)
    Page<PedidoVenda> buscarResumoPorStatus(
            @Param("status") StatusPedido status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"usuario", "venda"})
    @Query("""
            SELECT pv
            FROM PedidoVenda pv
            WHERE pv.id = :id
              AND (:status IS NULL OR pv.status = :status)
            """)
    Page<PedidoVenda> buscarResumoPorIdEStatus(
            @Param("id") UUID id,
            @Param("status") StatusPedido status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"usuario", "venda"})
    @Query("""
            SELECT pv
            FROM PedidoVenda pv
            JOIN pv.usuario u
            WHERE (:status IS NULL OR pv.status = :status)
              AND (
                    LOWER(u.nome) LIKE CONCAT('%', :search, '%')
                    OR LOWER(u.sobrenome) LIKE CONCAT('%', :search, '%')
              )
            """)
    Page<PedidoVenda> buscarResumoPorNomeEStatus(
            @Param("search") String search,
            @Param("status") StatusPedido status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"usuario", "venda"})
    @Query("""
            SELECT pv
            FROM PedidoVenda pv
            WHERE pv.usuario.id = :usuarioId
              AND (:status IS NULL OR pv.status = :status)
            """)
    Page<PedidoVenda> buscarResumoPorUsuarioId(
            @Param("usuarioId") UUID usuarioId,
            @Param("status") StatusPedido status,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT pv
            FROM PedidoVenda pv
            LEFT JOIN FETCH pv.usuario
            LEFT JOIN FETCH pv.venda
            LEFT JOIN FETCH pv.itens i
            LEFT JOIN FETCH i.produto
            WHERE pv.id = :id
            """)
    Optional<PedidoVenda> buscarDetalhePorId(@Param("id") UUID id);

    @Query("""
        SELECT p.status, COUNT(p)
        FROM PedidoVenda p
        GROUP BY p.status
    """)
    List<Object[]> countGroupedByStatus();

    @Query("""
        SELECT u.nome, u.sobrenome, MAX(p.criadoEm) as ultimoPedido
        FROM PedidoVenda p
        JOIN p.usuario u
        WHERE p.status IN (
            com.zeroone.simlady.entity.enums.StatusPedido.PENDENTE,
            com.zeroone.simlady.entity.enums.StatusPedido.CONCLUIDO
        )
          AND u.permissao = com.zeroone.simlady.entity.enums.Permissao.COMUM
        GROUP BY u.id, u.nome, u.sobrenome
        HAVING MAX(p.criadoEm) < :corte
        ORDER BY MAX(p.criadoEm) ASC
    """)
    List<Object[]> findClientesInativos(@Param("corte") LocalDateTime corte, Pageable pageable);
}
