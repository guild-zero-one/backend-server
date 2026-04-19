package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
}
