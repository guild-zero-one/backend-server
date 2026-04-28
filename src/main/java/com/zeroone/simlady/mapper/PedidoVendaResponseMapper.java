package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.pedido.*;
import com.zeroone.simlady.entity.PedidoItem;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.entity.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class PedidoVendaResponseMapper {

    public PedidoResumoResponseDto toResumo(PedidoVenda pedidoVenda) {
        PedidoResumoResponseDto dto = new PedidoResumoResponseDto();
        dto.setId(pedidoVenda.getId());
        dto.setStatus(pedidoVenda.getStatus().name());
        dto.setIdVenda(pedidoVenda.getVenda() != null ? pedidoVenda.getVenda().getId() : null);
        dto.setCriadoEm(pedidoVenda.getCriadoEm());
        dto.setAtualizadoEm(pedidoVenda.getAtualizadoEm());
        dto.setTotalItens(calcularTotalItens(pedidoVenda.getItens()));
        dto.setTotalValorVenda(calcularTotalValorVenda(pedidoVenda.getItens()));
        dto.setUsuario(toUsuarioResumo(pedidoVenda.getUsuario()));
        return dto;
    }

    public PedidoDetalheResponseDto toDetalhe(PedidoVenda pedidoVenda) {
        PedidoDetalheResponseDto dto = new PedidoDetalheResponseDto();
        dto.setId(pedidoVenda.getId());
        dto.setStatus(pedidoVenda.getStatus().name());
        dto.setIdVenda(pedidoVenda.getVenda() != null ? pedidoVenda.getVenda().getId() : null);
        dto.setCriadoEm(pedidoVenda.getCriadoEm());
        dto.setAtualizadoEm(pedidoVenda.getAtualizadoEm());
        dto.setTotalItens(calcularTotalItens(pedidoVenda.getItens()));
        dto.setTotalPrecoUnitario(calcularTotalPrecoUnitario(pedidoVenda.getItens()));
        dto.setTotalValorVenda(calcularTotalValorVenda(pedidoVenda.getItens()));
        dto.setUsuario(toUsuarioResumo(pedidoVenda.getUsuario()));
        dto.setItens(toItensDetalhe(pedidoVenda.getItens()));
        return dto;
    }

    private UsuarioResumoPedidoResponseDto toUsuarioResumo(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResumoPedidoResponseDto dto = new UsuarioResumoPedidoResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setUrlImagem(usuario.getUrlImagem());
        dto.setCelular(usuario.getCelular());
        dto.setAtivo(usuario.getAtivo());
        return dto;
    }

    private List<PedidoItemDetalheResponseDto> toItensDetalhe(List<PedidoItem> itens) {
        if (itens == null) {
            return List.of();
        }

        return itens.stream()
                .map(this::toItemDetalhe)
                .toList();
    }

    private PedidoItemDetalheResponseDto toItemDetalhe(PedidoItem item) {
        PedidoItemDetalheResponseDto dto = new PedidoItemDetalheResponseDto();
        dto.setId(item.getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setValorVenda(item.getValorVenda());
        dto.setProduto(toProdutoResumo(item.getProduto()));
        return dto;
    }

    private ProdutoResumoPedidoResponseDto toProdutoResumo(Produto produto) {
        if (produto == null) {
            return null;
        }

        ProdutoResumoPedidoResponseDto dto = new ProdutoResumoPedidoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setUrlImagem(produto.getUrlImagem());
        return dto;
    }

    private Integer calcularTotalItens(List<PedidoItem> itens) {
        if (itens == null) {
            return 0;
        }

        return itens.stream()
                .filter(Objects::nonNull)
                .map(PedidoItem::getQuantidade)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    private BigDecimal calcularTotalPrecoUnitario(List<PedidoItem> itens) {
        if (itens == null) {
            return BigDecimal.ZERO;
        }

        return itens.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getPrecoUnitario() != null && item.getQuantidade() != null)
                .map(item -> item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalValorVenda(List<PedidoItem> itens) {
        if (itens == null) {
            return BigDecimal.ZERO;
        }

        return itens.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getPrecoUnitario() != null && item.getQuantidade() != null)
                .map(item -> {
                    BigDecimal valorVenda = item.getValorVenda() != null ? item.getValorVenda() : item.getPrecoUnitario();
                    return valorVenda.multiply(BigDecimal.valueOf(item.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
