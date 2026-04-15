package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.categoria.CategoriaRequestDto;
import com.zeroone.simlady.dto.categoria.CategoriaResponseDto;
import com.zeroone.simlady.entity.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria toEntity(CategoriaRequestDto dto);

    CategoriaResponseDto toResponseDto(Categoria categoria);
}

