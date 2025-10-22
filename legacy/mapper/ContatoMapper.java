package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.contato.ContatoRequestDto;
import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.entity.Contato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ContatoMapper {
    Contato toEntity(ContatoRequestDto contatoRequestDto);

    @Mapping(source = "usuario.id", target = "fkUsuario")
    ContatoResponseDto toDto(Contato contato);

}
