package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemPatchDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemRequestDto;
import com.zeroone.simlady.dto.produtoImagem.ProdutoImagemResponseDto;
import com.zeroone.simlady.entity.ProdutoImagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoImagemMapper {

    @Mapping(target = "produto.id", source = "produtoId")
    ProdutoImagem toEntity(ProdutoImagemRequestDto request);

    @Mapping(target = "produto.id", source = "produtoId")
    ProdutoImagem toEntity(ProdutoImagemPatchDto request);

    @Mapping(target = "produtoId", source = "produto.id")
    ProdutoImagemResponseDto toResponseDto(ProdutoImagem produtoImagem);
}
