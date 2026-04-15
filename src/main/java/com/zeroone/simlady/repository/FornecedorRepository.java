package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
    Optional<Fornecedor> findByNomeIgnoreCase(String nome);

    Page<Fornecedor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
