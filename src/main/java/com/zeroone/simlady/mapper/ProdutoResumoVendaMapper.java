package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.produto.ProdutoResumoVendaDto;
import com.zeroone.simlady.entity.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoResumoVendaMapper {
    ProdutoResumoVendaDto toDto(Produto entity);
}

