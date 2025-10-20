package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;
import com.zeroone.simlady.infrastructure.persistance.entity.FornecedorEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.FornecedorMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.FornecedorRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FornecedorJpaAdapter implements FornecedorRepositoryPort {

    public final FornecedorRepositoryImpl repository;

    @Override
    public Fornecedor salvarFornecedor (Fornecedor fornecedor){
        FornecedorEntity entity = FornecedorMapper.toEntity(fornecedor);
        FornecedorEntity saved = repository.save(entity);

        return FornecedorMapper.toRawFornecedor(saved);
    }

    @Override
    public Optional<Fornecedor> buscarPorId(UUID id){
        return repository.findById(id)
                .map(FornecedorMapper::toRawFornecedor);
    }

    @Override
    public void deletarPorId(UUID id){
        repository.deleteById(id);
    }

    @Override
    public Fornecedor atualizarFornecedor(Fornecedor fornecedor){
        FornecedorEntity entity = FornecedorMapper.toEntity(fornecedor);
        FornecedorEntity saved = repository.save(entity);

        return FornecedorMapper.toRawFornecedor(saved);
    }

    @Override
    public Page<Fornecedor> listarTodos(int pagina, int tamanho){
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        Page<FornecedorEntity> pageEntity = repository.findAll(pageable);
        return pageEntity.map(FornecedorMapper::toRawFornecedor);

    }

    @Override
    public Page<Fornecedor> listarComProdutos(int pagina, int tamanho){
        return null;
    }

}
