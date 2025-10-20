package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.infrastructure.persistance.entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FornecedorRepositoryImpl extends JpaRepository <FornecedorEntity, UUID> {
}
