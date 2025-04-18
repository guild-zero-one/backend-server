package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {

}
