package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import com.zeroone.simlady.infrastructure.persistance.entity.PedidoEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.PedidoMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.PedidoRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PedidoJpaAdapter implements PedidoRepositoryPort {

    private final PedidoRepositoryImpl repository;

    @Override
    public Pedido salvarPedido(Pedido pedido) {
        PedidoEntity entity = PedidoMapper.toEntity(pedido);
        PedidoEntity saved = repository.save(entity);
        return PedidoMapper.toDomain(saved);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(PedidoMapper::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Pedido atualizarPedido(Pedido pedido) {
        PedidoEntity entity = PedidoMapper.toEntity(pedido);
        PedidoEntity saved = repository.save(entity);
        return PedidoMapper.toDomain(saved);
    }

    @Override
    public Page<Pedido> listarTodos(int pagina, int tamanho) {
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        Page<PedidoEntity> pageEntity = repository.findAll(pageable);
        return pageEntity.map(PedidoMapper::toDomain);
    }

    @Override
    public Page<Pedido> listarPorUsuario(UUID idUsuario, int pagina, int tamanho) {
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        Page<PedidoEntity> pageEntity = repository.findByIdUsuario(idUsuario, pageable);
        return pageEntity.map(PedidoMapper::toDomain);
    }

    @Override
    public Page<Pedido> listarPorStatus(String status, int pagina, int tamanho) {
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        // Converte String para enum StatusPedido
        StatusPedido statusEnum = StatusPedido.valueOf(status.toUpperCase());
        Page<PedidoEntity> pageEntity = repository.findByStatus(statusEnum, pageable);
        return pageEntity.map(PedidoMapper::toDomain);
    }

    @Override
    public Page<Pedido> listarPorVenda(UUID idVenda, int pagina, int tamanho) {
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        Page<PedidoEntity> pageEntity = repository.findByIdVenda(idVenda, pageable);
        return pageEntity.map(PedidoMapper::toDomain);
    }

    @Override
    public long contarPedidosPorUsuario(UUID idUsuario) {
        return repository.countByIdUsuario(idUsuario);
    }
}
