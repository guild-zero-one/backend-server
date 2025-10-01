package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.entity.ProdutoEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.ProdutoMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.ProdutoRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProdutoJpaAdapter implements ProdutoRepository {
    private final ProdutoRepositoryImpl repository;

    @Override
    public Produto salvarProduto(Produto produto) {
        ProdutoEntity entity = ProdutoMapper.toEntity(produto);
        ProdutoEntity saved = repository.save(entity);
        return ProdutoMapper.toRawProduto(saved);
    }

    @Override
    public List<Produto> listar() {
        List<ProdutoEntity> listProduto = repository.findAll();
        return listProduto.stream().map(ProdutoMapper::toRawProduto).toList();
    }
}
