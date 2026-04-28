package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.venda.UsuarioResumoVendaResponseDto;
import com.zeroone.simlady.dto.venda.VendaResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.Venda;
import org.springframework.stereotype.Component;

@Component
public class VendaResponseMapper {

    private final PedidoVendaResponseMapper pedidoVendaResponseMapper;

    public VendaResponseMapper(PedidoVendaResponseMapper pedidoVendaResponseMapper) {
        this.pedidoVendaResponseMapper = pedidoVendaResponseMapper;
    }

    public VendaResponseDto toResumo(Venda venda) {
        VendaResponseDto dto = toBase(venda);
        PedidoVenda pedidoPrincipal = obterPedidoPrincipal(venda);
        if (pedidoPrincipal != null) {
            dto.setUsuario(toUsuarioResumo(pedidoPrincipal.getUsuario()));
        }
        return dto;
    }

    public VendaResponseDto toDetalhe(Venda venda) {
        VendaResponseDto dto = toBase(venda);
        PedidoVenda pedidoPrincipal = obterPedidoPrincipal(venda);
        if (pedidoPrincipal != null) {
            dto.setUsuario(toUsuarioResumo(pedidoPrincipal.getUsuario()));
            dto.setPedido(pedidoVendaResponseMapper.toDetalhe(pedidoPrincipal));
        }
        return dto;
    }

    private VendaResponseDto toBase(Venda venda) {
        VendaResponseDto dto = new VendaResponseDto();
        dto.setId(venda.getId());
        dto.setValorTotal(venda.getValorTotal());
        dto.setDesconto(venda.getDesconto());
        dto.setPagamentoRealizado(venda.getPagamentoRealizado());
        dto.setDataVenda(venda.getDataVenda());
        return dto;
    }

    private UsuarioResumoVendaResponseDto toUsuarioResumo(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResumoVendaResponseDto dto = new UsuarioResumoVendaResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setUrlImagem(usuario.getUrlImagem());
        dto.setAtivo(usuario.getAtivo());
        return dto;
    }

    private PedidoVenda obterPedidoPrincipal(Venda venda) {
        if (venda.getPedidos() == null || venda.getPedidos().isEmpty()) {
            return null;
        }
        return venda.getPedidos().getFirst();
    }
}
