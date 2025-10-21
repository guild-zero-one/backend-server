package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.infrastructure.persistance.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface VendaRepositoryImpl extends JpaRepository<VendaEntity, UUID> {
    
    @Query("SELECT v FROM VendaEntity v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    Page<VendaEntity> findByDataVendaBetween(@Param("dataInicio") LocalDate dataInicio, 
                                           @Param("dataFim") LocalDate dataFim, 
                                           Pageable pageable);
    
    Page<VendaEntity> findByPagamentoRealizado(Boolean pagamentoRealizado, Pageable pageable);
}
