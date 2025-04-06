package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.cliente.ClienteRequestDto;
import com.zeroone.simlady.dto.cliente.ClienteResponseDto;
import com.zeroone.simlady.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PedidoVendaMapper.class, ContatoMapper.class})
public interface ClienteMapper {

    Cliente toEntity(ClienteRequestDto dto);

    ClienteResponseDto toDto(Cliente cliente);
}


