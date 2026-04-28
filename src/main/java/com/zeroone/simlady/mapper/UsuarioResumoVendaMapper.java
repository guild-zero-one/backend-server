package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.usuario.UsuarioResumoVendaDto;
import com.zeroone.simlady.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioResumoVendaMapper {
    UsuarioResumoVendaDto toDto(Usuario entity);
}

