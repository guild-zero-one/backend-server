package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.pedido.*;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.infrastructure.persistance.entity.PedidoEntity;
import com.zeroone.simlady.infrastructure.persistance.entity.PedidoItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {
    
    public static PedidoResponseDto toResponseDto(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getIdVenda(),
                pedido.getIdUsuario(),
                pedido.getItens().stream()
                        .map(PedidoMapper::toItemResponseDto)
                        .toList(),
                pedido.calcularTotal().getValor().toString(),
                pedido.getCriadoEm(),
                pedido.getAtualizadoEm()
        );
    }
    
    public static PedidoItemResponseDto toItemResponseDto(PedidoItem item) {
        if (item == null) {
            return null;
        }
        
        return new PedidoItemResponseDto(
                item.getId(),
                item.getIdProduto(),
                item.getQuantidade().getValor(),
                item.getPrecoUnitario().getValor().toString(),
                item.calcularSubtotal().getValor().toString()
        );
    }
    
    public static Pedido toDomain(PedidoCreateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Pedido.newPedido(null, dto.idUsuario());
    }
    
    public static Pedido toDomain(PedidoUpdateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Pedido.newPedido(dto.idVenda(), dto.idUsuario());
    }
    
    public static PedidoItem toDomain(PedidoItemRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return PedidoItem.newPedidoItem(
                dto.idProduto(),
                dto.quantidade(),
                dto.precoUnitario().toString()
        );
    }
    
    public static PedidoEntity toEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        PedidoEntity entity = new PedidoEntity(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getIdVenda(),
                pedido.getIdUsuario(),
                null,
                pedido.getCriadoEm(),
                pedido.getAtualizadoEm()
        );
        
        if (pedido.getItens() != null) {
            List<PedidoItemEntity> itensEntity = pedido.getItens().stream()
                    .map(item -> PedidoMapper.toEntity(item, entity))
                    .collect(Collectors.toList());
            entity.setItens(itensEntity);
        }
        
        return entity;
    }
    
    public static Pedido toDomain(PedidoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        List<PedidoItem> itens = null;
        if (entity.getItens() != null) {
            itens = entity.getItens().stream()
                    .map(PedidoMapper::toDomain)
                    .collect(Collectors.toList());
        }
        
        return Pedido.of(
                entity.getId(),
                entity.getStatus(),
                entity.getIdVenda(),
                entity.getIdUsuario(),
                itens,
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
    
    public static PedidoItemEntity toEntity(PedidoItem item, PedidoEntity pedido) {
        if (item == null) {
            return null;
        }
        
        return new PedidoItemEntity(
                item.getId(),
                item.getIdProduto(),
                pedido,
                item.getQuantidade().getValor(),
                item.getPrecoUnitario().getValor()
        );
    }
    
    public static PedidoItem toDomain(PedidoItemEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return PedidoItem.of(
                entity.getId(),
                entity.getIdProduto(),
                entity.getQuantidade(),
                entity.getPrecoUnitario().toString()
        );
    }
}