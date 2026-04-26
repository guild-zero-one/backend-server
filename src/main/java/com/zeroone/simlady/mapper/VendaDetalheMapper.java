package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.venda.VendaDetalheDto;
import com.zeroone.simlady.entity.Venda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PedidoDetalheVendaMapper.class})
public interface VendaDetalheMapper {
    @Mapping(target = "pedido", ignore = true)
    VendaDetalheDto toDto(Venda entity);
}

