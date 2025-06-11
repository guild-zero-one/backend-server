package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByFornecedorId(Integer fornecedorId);

    Produto findProdutoBySkuIgnoreCase(String sku);
}
