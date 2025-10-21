package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.infrastructure.persistance.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepositoryImpl extends JpaRepository<ProdutoEntity, UUID> {
    
    Optional<ProdutoEntity> findBySku(String sku);
    
    Page<ProdutoEntity> findByCatalogo(Boolean catalogo, Pageable pageable);
    
    Page<ProdutoEntity> findByIdFornecedor(UUID idFornecedor, Pageable pageable);
}
