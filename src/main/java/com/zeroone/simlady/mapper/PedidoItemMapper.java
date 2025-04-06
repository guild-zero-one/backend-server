package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.item.PedidoItemRequestDto;
import com.zeroone.simlady.dto.item.PedidoItemResponseDto;
import com.zeroone.simlady.entity.PedidoItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PedidoItemMapper {

    @Mapping(target = "loteProduto.id", source = "idLoteProduto")
    PedidoItem toEntity(PedidoItemRequestDto dto);

    @Mapping(target = "idLoteProduto", source = "loteProduto.id")
    PedidoItemResponseDto toDto(PedidoItem pedido);
}




