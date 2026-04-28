package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedido.PedidoDetalheVendaDto;
import com.zeroone.simlady.entity.PedidoVenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

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
    default Integer calculateTotalItens(List<com.zeroone.simlady.entity.PedidoItem> itens) {
        return itens != null ? itens.size() : 0;
    }

    @Named("calculateTotalPrecoUnitario")
    default BigDecimal calculateTotalPrecoUnitario(List<com.zeroone.simlady.entity.PedidoItem> itens) {
        if (itens == null || itens.isEmpty()) return BigDecimal.ZERO;
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateTotalValorVenda")
    default BigDecimal calculateTotalValorVenda(List<com.zeroone.simlady.entity.PedidoItem> itens) {
        if (itens == null || itens.isEmpty()) return BigDecimal.ZERO;
        return itens.stream()
                .map(item -> {
                    BigDecimal valorVenda = (item.getProduto() != null && item.getProduto().getValorVenda() != null)
                            ? BigDecimal.valueOf(item.getProduto().getValorVenda())
                            : BigDecimal.ZERO;
                    return valorVenda.multiply(BigDecimal.valueOf(item.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

