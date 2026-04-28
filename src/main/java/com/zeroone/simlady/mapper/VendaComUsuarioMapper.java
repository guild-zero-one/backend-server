package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.venda.VendaComUsuarioDto;
import com.zeroone.simlady.entity.Venda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioResumoVendaMapper.class})
public interface VendaComUsuarioMapper {
    @Mapping(target = "usuario", ignore = true)
    VendaComUsuarioDto toDto(Venda entity);

    List<VendaComUsuarioDto> toDto(List<Venda> entities);
}


