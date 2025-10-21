package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.ProdutoRepository;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.entity.ProdutoEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.ProdutoMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.ProdutoRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProdutoJpaAdapter implements ProdutoRepository {
    
    private final ProdutoRepositoryImpl repository;
    private final ProdutoMapper mapper;
    
    public ProdutoJpaAdapter(ProdutoRepositoryImpl repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public Produto salvarProduto(Produto produto) {
        ProdutoEntity entity = mapper.toEntity(produto);
        ProdutoEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Produto> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public void deletarPorId(UUID id) {
        repository.deleteById(id);
    }
    
    @Override
    public Produto atualizarProduto(Produto produto) {
        ProdutoEntity entity = mapper.toEntity(produto);
        ProdutoEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }
    
    @Override
    public Page<Produto> listarTodos(int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findAll(pageRequest)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        return repository.findBySku(sku)
                .map(mapper::toDomain);
    }
    
    @Override
    public Page<Produto> listarPorFornecedor(UUID idFornecedor, int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findByIdFornecedor(idFornecedor, pageRequest)
                .map(mapper::toDomain);
    }
    
    @Override
    public Page<Produto> listarPorCatalogo(Boolean catalogo, int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findByCatalogo(catalogo, pageRequest)
                .map(mapper::toDomain);
    }
}
