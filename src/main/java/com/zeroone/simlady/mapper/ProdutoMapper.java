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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Produto toEntity (ProdutoRequestDto request);
}
