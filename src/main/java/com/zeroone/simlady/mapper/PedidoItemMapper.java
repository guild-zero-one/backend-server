package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemRequestDto;
import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import com.zeroone.simlady.entity.PedidoItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PedidoItemMapper {
    @Mapping(target = "produto.id", source = "idProduto")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidoVenda", ignore = true)
    PedidoItem toEntity(PedidoItemRequestDto dto);

    @Mapping(target = "idProduto", source = "produto.id")
    @Mapping(target = "valorVenda", source = "produto.valorVenda")
    PedidoItemResponseDto toDto(PedidoItem pedido);
}




