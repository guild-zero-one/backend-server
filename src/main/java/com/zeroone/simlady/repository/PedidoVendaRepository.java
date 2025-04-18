package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Integer> {
    List<PedidoVenda> findAllByStatus(StatusPedido status);

    List<PedidoVenda> findAllByCliente(Cliente cliente);
}
