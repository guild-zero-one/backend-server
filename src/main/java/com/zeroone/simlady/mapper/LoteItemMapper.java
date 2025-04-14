package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.entity.LoteItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoteItemMapper {
    LoteItem toEntity(LoteItemRequestDto loteItemRequestDto);
}
