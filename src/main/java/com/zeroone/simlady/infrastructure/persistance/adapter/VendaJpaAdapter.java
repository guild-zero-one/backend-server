package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.infrastructure.persistance.entity.VendaEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.VendaMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.VendaRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
public class VendaJpaAdapter implements VendaRepositoryPort {
    
    private final VendaRepositoryImpl repository;
    private final VendaMapper mapper;
    
    public VendaJpaAdapter(VendaRepositoryImpl repository, VendaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public Venda salvarVenda(Venda venda) {
        VendaEntity entity = mapper.toEntity(venda);
        VendaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Venda> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public void deletarPorId(UUID id) {
        repository.deleteById(id);
    }
    
    @Override
    public Venda atualizarVenda(Venda venda) {
        VendaEntity entity = mapper.toEntity(venda);
        VendaEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }
    
    @Override
    public Page<Venda> listarTodas(int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findAll(pageRequest)
                .map(mapper::toDomain);
    }
    
    
    @Override
    public Page<Venda> listarPorDataVenda(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findByDataVendaBetween(dataInicio, dataFim, pageRequest)
                .map(mapper::toDomain);
    }
    
    @Override
    public Page<Venda> listarPorPagamentoRealizado(Boolean pagamentoRealizado, int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return repository.findByPagamentoRealizado(pagamentoRealizado, pageRequest)
                .map(mapper::toDomain);
    }
}
