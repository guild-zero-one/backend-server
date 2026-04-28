package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemComProdutoDto;
import com.zeroone.simlady.entity.PedidoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProdutoResumoVendaMapper.class})
public interface PedidoItemComProdutoMapper {
    @Mapping(target = "produto", source = "produto")
    @Mapping(target = "valorVenda", source = "produto.valorVenda")
    PedidoItemComProdutoDto toDto(PedidoItem entity);

    List<PedidoItemComProdutoDto> toDto(List<PedidoItem> entities);
}

