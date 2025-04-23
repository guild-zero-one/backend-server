package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import com.zeroone.simlady.entity.LoteItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LoteItemMapperHelper.class)
public interface LoteItemMapper {
    @Mapping(target = "qtdLoteCompra", source = "qtdLoteCompra")
    @Mapping(target = "valorUnitarioCompra", source = "valorUnitarioCompra")
    @Mapping(target = "dataValidade", source = "dataValidade")
    @Mapping(target = "produto", source = "produtoId")
    @Mapping(target = "lote", source = "loteId")
    LoteItem toEntity(LoteItemRequestDto loteItemRequestDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "qtdLoteCompra", source = "qtdLoteCompra")
    @Mapping(target = "valorUnitarioCompra", source = "valorUnitarioCompra")
    @Mapping(target = "dataValidade", source = "dataValidade")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "loteId", source = "lote.id")
    LoteItemResponseDto toResponseDto(LoteItem loteItem);
}