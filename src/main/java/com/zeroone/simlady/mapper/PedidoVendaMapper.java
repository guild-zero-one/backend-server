package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedido.PedidoVendaRequestDto;
import com.zeroone.simlady.dto.pedido.PedidoVendaResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PedidoItemMapper.class})
public interface PedidoVendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "venda", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "usuario.id", source = "idUsuario")
    @Mapping(target = "itens", source = "itens")
    PedidoVenda toEntity(PedidoVendaRequestDto dto);

    @Mapping(target = "idUsuario", source = "usuario.id")
    @Mapping(target = "idVenda", source = "venda.id")
    @Mapping(target = "itens", source = "itens")
    PedidoVendaResponseDto toDto(PedidoVenda entity);

    List<PedidoVendaResponseDto> toDto(List<PedidoVenda> pedidos);

    default List<PedidoVendaResponseDto> toDto(Set<PedidoVenda> pedidos) {
        if (pedidos == null) {
            return List.of();
        }
        return pedidos.stream().map(this::toDto).collect(Collectors.toList());
    }
}
