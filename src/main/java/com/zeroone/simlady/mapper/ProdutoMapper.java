package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.produto.ProdutoRequestDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    ProdutoResponseDto toResponseDto (Produto produto);

    Produto toEntity (ProdutoRequestDto request);
}
