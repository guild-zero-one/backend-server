package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.categoria.CategoriaRequestDto;
import com.zeroone.simlady.dto.categoria.CategoriaResponseDto;
import com.zeroone.simlady.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Categoria toEntity(CategoriaRequestDto dto);

    CategoriaResponseDto toResponseDto(Categoria categoria);
}


