package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.dto.cliente.ClienteRequestDto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.dto.cliente.ClienteResponseDto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    Cliente toEntity (ClienteRequestDto dto);

    ClienteResponseDto toDto(Cliente cliente);

}

