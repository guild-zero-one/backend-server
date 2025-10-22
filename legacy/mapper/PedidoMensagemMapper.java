package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedido.PedidoMensagemDto;
import com.zeroone.simlady.entity.PedidoVenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PedidoItemMapper.class})
public interface PedidoMensagemMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeUsuario", source = "usuario.nome")
    @Mapping(target = "contatosUsuario", ignore = true)
    @Mapping(target = "itens", source = "itens")
    PedidoMensagemDto toMessageDto(PedidoVenda pedido);
}
