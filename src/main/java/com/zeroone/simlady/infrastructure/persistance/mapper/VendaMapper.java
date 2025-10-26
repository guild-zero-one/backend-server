package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.venda.VendaCreateRequestDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaResponseDto;
import com.zeroone.simlady.core.adapters.dtos.venda.VendaUpdateRequestDto;
import com.zeroone.simlady.core.domain.venda.Venda;
import com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto;
import com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal;
import com.zeroone.simlady.infrastructure.persistance.entity.VendaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendaMapper {
        
    public Venda toDomain(VendaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Venda.of(
                entity.getId(),
                ValorTotal.of(entity.getValorTotal()),
                Desconto.of(entity.getDesconto()),
                entity.getPagamentoRealizado(),
                entity.getDataVenda(),
                entity.getPedidosIds(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
    
    public VendaEntity toEntity(Venda domain) {
        if (domain == null) {
            return null;
        }
        
        return new VendaEntity(
                domain.getId(),
                domain.getValorTotal().getValor(),
                domain.getDesconto().getValor(),
                domain.getPagamentoRealizado(),
                domain.getDataVenda(),
                domain.getPedidosIds(),
                domain.getCriadoEm(),
                domain.getAtualizadoEm()
        );
    }
    
    public List<Venda> toDomainList(List<VendaEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
        
    public VendaResponseDto toResponseDto(Venda venda) {
        if (venda == null) {
            return null;
        }
        
        VendaResponseDto dto = new VendaResponseDto();
        dto.setId(venda.getId());
        dto.setValorTotal(venda.getValorTotal().getValor().toString());
        dto.setDesconto(venda.getDesconto().getValor().toString());
        dto.setValorFinal(venda.calcularValorFinal().getValor().toString());
        dto.setPagamentoRealizado(venda.getPagamentoRealizado());
        dto.setDataVenda(venda.getDataVenda());
        dto.setPedidosIds(venda.getPedidosIds());
        dto.setCriadoEm(venda.getCriadoEm());
        dto.setAtualizadoEm(venda.getAtualizadoEm());
        
        return dto;
    }
    
    public Venda toDomainFromCreateRequest(VendaCreateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Venda.newVenda(
                ValorTotal.of(dto.getValorTotal()),
                Desconto.of(dto.getDesconto()),
                dto.getDataVenda()
        );
    }
    
    public Venda toDomainFromUpdateRequest(VendaUpdateRequestDto dto, Venda vendaExistente) {
        if (dto == null || vendaExistente == null) {
            return vendaExistente;
        }
        
        return Venda.of(
                vendaExistente.getId(),
                dto.getValorTotal() != null ? 
                    ValorTotal.of(dto.getValorTotal()) : 
                    vendaExistente.getValorTotal(),
                dto.getDesconto() != null ? 
                    Desconto.of(dto.getDesconto()) : 
                    vendaExistente.getDesconto(),
                vendaExistente.getPagamentoRealizado(),
                dto.getDataVenda() != null ? dto.getDataVenda() : vendaExistente.getDataVenda(),
                dto.getPedidosIds() != null ? dto.getPedidosIds() : vendaExistente.getPedidosIds(),
                vendaExistente.getCriadoEm(),
                vendaExistente.getAtualizadoEm()
        );
    }
    
    public List<VendaResponseDto> toResponseDtoList(List<Venda> vendas) {
        return vendas.stream()
                .map(this::toResponseDto)
                .toList();
    }
}