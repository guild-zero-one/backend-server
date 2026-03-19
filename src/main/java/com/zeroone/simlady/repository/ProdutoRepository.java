package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByFornecedorId(UUID fornecedorId);

    Produto findProdutoBySkuIgnoreCase(String sku);
}
