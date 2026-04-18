package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByNomeIgnoreCase(String nome);

    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT c FROM Categoria c JOIN c.produtos p WHERE p.id = :produtoId")
    List<Categoria> findByProdutosId(@Param("produtoId") UUID produtoId);
}

