package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.infrastructure.persistance.entity.PedidoEntity;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoRepositoryImpl extends JpaRepository<PedidoEntity, UUID> {
    Page<PedidoEntity> findByIdUsuario(UUID idUsuario, Pageable pageable);
    
    Page<PedidoEntity> findByStatus(StatusPedido status, Pageable pageable);
    
    Page<PedidoEntity> findByIdVenda(UUID idVenda, Pageable pageable);
    
    Page<PedidoEntity> findByIdUsuarioAndStatus(UUID idUsuario, StatusPedido status, Pageable pageable);

    Page<PedidoEntity> findByIdUsuarioOrderByCriadoEmDesc(UUID idUsuario, Pageable pageable);
    
    long countByStatus(StatusPedido status);
    
    long countByIdUsuario(UUID idUsuario);
    
    long countByIdUsuarioAndStatus(UUID idUsuario, StatusPedido status);
    
    boolean existsByIdUsuario(UUID idUsuario);
}
