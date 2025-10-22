package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.ProdutoImagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoImagemRepository extends JpaRepository<ProdutoImagem, Integer> {
    List<ProdutoImagem> findByProdutoId(Integer fornecedorId);
}
