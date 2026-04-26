package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.venda.VendaComUsuarioDto;
import com.zeroone.simlady.entity.Venda;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioResumoVendaMapper.class})
public interface VendaComUsuarioMapper {
    VendaComUsuarioDto toDto(Venda entity);

    List<VendaComUsuarioDto> toDto(List<Venda> entities);
}


