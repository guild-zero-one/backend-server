package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedido.PedidoDetalheVendaDto;
import com.zeroone.simlady.entity.PedidoVenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {UsuarioResumoVendaMapper.class, PedidoItemComProdutoMapper.class})
public interface PedidoDetalheVendaMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "idVenda", source = "venda.id")
    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "totalItens", source = "itens", qualifiedByName = "calculateTotalItens")
    @Mapping(target = "totalPrecoUnitario", source = "itens", qualifiedByName = "calculateTotalPrecoUnitario")
    @Mapping(target = "totalValorVenda", source = "itens", qualifiedByName = "calculateTotalValorVenda")
    @Mapping(target = "itens", source = "itens")
    PedidoDetalheVendaDto toDto(PedidoVenda entity);

    @Named("calculateTotalItens")
    default Integer calculateTotalItens(Set<com.zeroone.simlady.entity.PedidoItem> itens) {
        return itens != null ? itens.size() : 0;
    }

    @Named("calculateTotalPrecoUnitario")
    default BigDecimal calculateTotalPrecoUnitario(Set<com.zeroone.simlady.entity.PedidoItem> itens) {
        if (itens == null || itens.isEmpty()) return BigDecimal.ZERO;
        return itens.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getPrecoUnitario() != null && item.getQuantidade() != null)
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateTotalValorVenda")
    default BigDecimal calculateTotalValorVenda(Set<com.zeroone.simlady.entity.PedidoItem> itens) {
        if (itens == null || itens.isEmpty()) return BigDecimal.ZERO;
        return itens.stream()
                .filter(Objects::nonNull)
                .filter(item -> (item.getValorVenda() != null || item.getPrecoUnitario() != null) && item.getQuantidade() != null)
                .map(item -> {
                    BigDecimal valorVenda = item.getValorVenda() != null ? item.getValorVenda() : item.getPrecoUnitario();
                    return valorVenda.multiply(BigDecimal.valueOf(item.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
