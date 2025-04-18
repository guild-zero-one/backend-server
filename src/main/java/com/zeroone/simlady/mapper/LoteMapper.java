package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.lote.LoteRequestDto;
import com.zeroone.simlady.dto.lote.LoteResponseDto;
import com.zeroone.simlady.dto.lote.LoteResponseItemDto;
import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.dto.loteItem.LoteItemResponseDto;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.entity.LoteItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoteMapper {

    Lote toEntity(LoteRequestDto dto);

    @Mapping(target = "valorTotal", source = "valorTotal")
    @Mapping(target = "qtdLote", source = "qtdLote")
    @Mapping(target = "id", source = "id")
    LoteResponseDto toResponseDto(Lote lote);

    LoteResponseItemDto toResponseItemDto(Lote lote, List<LoteItemResponseDto> loteItems);
}
