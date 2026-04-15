package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByNomeIgnoreCase(String nome);

    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}

