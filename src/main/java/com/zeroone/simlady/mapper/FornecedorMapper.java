package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.fornecedor.FornecedorRequestDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface FornecedorMapper {
    Fornecedor toEntity(FornecedorRequestDto dto);

    FornecedorResponseDto toResponseDto (Fornecedor fornecedor);
}
