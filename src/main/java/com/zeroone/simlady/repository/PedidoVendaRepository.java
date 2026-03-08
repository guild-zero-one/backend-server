package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Integer> {
    List<PedidoVenda> findAllByStatus(StatusPedido status);

    List<PedidoVenda> findAllByUsuario(Usuario usuario);

    Integer countPedidoVendasByUsuario_IdAndStatus(UUID usuarioId, StatusPedido statusPedido);

    Integer countByStatus(StatusPedido statusPedido);
}
