package com.zeroone.simlady.mapper;

import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.repository.LoteRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import org.springframework.stereotype.Component;

@Component
public class LoteItemMapperHelper {
    private final ProdutoRepository produtoRepository;
    private final LoteRepository loteRepository;

    public LoteItemMapperHelper(ProdutoRepository produtoRepository, LoteRepository loteRepository) {
        this.produtoRepository = produtoRepository;
        this.loteRepository = loteRepository;
    }

    public Produto mapProduto(Integer produtoId) {
        return produtoId == null ? null : produtoRepository.findById(produtoId).orElse(null);
    }

    public Lote mapLote(Integer loteId) {
        return loteId == null ? null : loteRepository.findById(loteId).orElse(null);
    }
}