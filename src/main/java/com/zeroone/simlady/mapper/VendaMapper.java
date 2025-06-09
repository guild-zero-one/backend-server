package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.venda.VendaRequestDto;
import com.zeroone.simlady.dto.venda.VendaResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Venda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendaMapper {
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "dataVenda", source = "dto.dataVenda")
    Venda toEntity(VendaRequestDto dto);


    VendaResponseDto toDto(Venda entity);

    List<VendaResponseDto> toDto(List<Venda> entities);
}