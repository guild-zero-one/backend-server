package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;
import com.zeroone.simlady.infrastructure.persistance.entity.MarcaEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.MarcaMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.MarcaRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MarcaJpaAdapter implements MarcaRepositoryPort {

    public final MarcaRepositoryImpl repository;

    @Override
    public Marca salvarMarca (Marca marca){
        MarcaEntity entity = MarcaMapper.toEntity(marca);
        MarcaEntity saved = repository.save(entity);

        return MarcaMapper.toRawMarca(saved);
    }

    @Override
    public Optional<Marca> buscarPorId(UUID id){
        return repository.findById(id)
                .map(MarcaMapper::toRawMarca);
    }

    @Override
    public void deletarPorId(UUID id){
        repository.deleteById(id);
    }

    @Override
    public Marca atualizarMarca(Marca marca){
        MarcaEntity entity = MarcaMapper.toEntity(marca);
        MarcaEntity saved = repository.save(entity);

        return MarcaMapper.toRawMarca(saved);
    }

    @Override
    public Page<Marca> listarTodos(int pagina, int tamanho){
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        Page<MarcaEntity> pageEntity = repository.findAll(pageable);
        return pageEntity.map(MarcaMapper::toRawMarca);
    }

    @Override
    public Page<Marca> listarComProdutos(int pagina, int tamanho){
        return null;
    }

}
