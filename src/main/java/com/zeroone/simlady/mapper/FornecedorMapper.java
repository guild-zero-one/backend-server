package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorRequestDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface FornecedorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Fornecedor toEntity(FornecedorRequestDto dto);

    @Mapping(target = "totalProdutos", ignore = true)
    FornecedorResponseDto toResponseDto (Fornecedor fornecedor);

    @Mapping(target = "produtos", source = "produtos")
    FornecedorComProdutosResponseDto toFornecedorComProdutosResponseDto(Fornecedor fornecedor, List<ProdutoResponseDto> produtos);
}
